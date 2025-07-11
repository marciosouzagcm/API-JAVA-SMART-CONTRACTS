package blockchain.api_java_smart_contracts.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import com.votacao.contracts.ContractVoting; // Importa a classe gerada pelo Web3j

/**
 * Classe de configuração Spring para definir os beans relacionados à interação
 * com a blockchain.
 * Garante que Web3j, Credenciais, Provedor de Gás e o Contrato de Votação sejam
 * inicializados corretamente e estejam disponíveis para injeção de dependência.
 */
@Configuration
public class BlockchainConfig {

    // Injeta a URL do nó Ethereum do application.properties
    @Value("${blockchain.ethereum.node-url}")
    private String nodeUrl;

    // Injeta a chave privada do administrador do application.properties
    @Value("${blockchain.admin.private-key}")
    private String adminPrivateKey;

    // Injeta o endereço do contrato Voting do application.properties
    @Value("${blockchain.voting.contract-address}")
    private String contractAddress;

    /**
     * Define o bean Web3j principal.
     * Este bean é marcado como @Primary para ser o preferencial para injeções de
     * Web3j
     * sem um qualificador específico, e também tem um qualificador explícito.
     * 
     * @return Uma instância de Web3j conectada ao nó Ethereum.
     */
    @Bean
    @Primary
    @Qualifier("web3jPrincipal")
    public Web3j web3jPrincipal() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define um bean Web3j para conexão geral, usado por outros serviços que
     * precisam interagir
     * com a blockchain.
     * 
     * @return Uma instância de Web3j conectada ao nó Ethereum.
     */
    @Bean
    @Qualifier("web3jConnection")
    public Web3j web3jConnection() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define um bean Web3j específico para o contrato, se houver lógica separada
     * que precise dele.
     * Embora web3jConnection seja suficiente, ter múltiplos beans do mesmo tipo com
     * qualificadores
     * diferentes pode ser útil para cenários mais complexos ou para clareza.
     * 
     * @return Uma instância de Web3j conectada ao nó Ethereum.
     */
    @Bean
    @Qualifier("contractWeb3j")
    public Web3j contractWeb3j() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define o bean de credenciais para o administrador do contrato, usando a chave
     * privada injetada.
     * Essas credenciais são usadas para assinar transações que interagem com o
     * contrato.
     * 
     * @return Uma instância de Credentials para o administrador.
     */
    @Bean
    @Qualifier("credentialsContract")
    public Credentials credentialsContract() {
        return Credentials.create(adminPrivateKey);
    }

    /**
     * Define o provedor de gás para as transações do contrato.
     * DefaultGasProvider usa valores padrão para gasPrice e gasLimit.
     * Você pode personalizar esses valores se necessário para sua rede.
     * 
     * @return Uma instância de ContractGasProvider.
     */
    @Bean
    public ContractGasProvider contractGasProvider() {
        // Exemplo de personalização:
        // return new DefaultGasProvider(BigInteger.valueOf(20_000_000_000L),
        // BigInteger.valueOf(6_700_000));
        return new DefaultGasProvider();
    }

    /**
     * Define o bean do contrato ContractVoting.
     * Ele injeta as dependências Web3j, Credentials e GasProvider necessárias
     * para carregar e interagir com uma instância do contrato já implantado.
     * 
     * @param web3j               Instância de Web3j para comunicação com a
     *                            blockchain.
     * @param credentials         Credenciais para assinar transações.
     * @param contractGasProvider Provedor de gás para as transações.
     * @return Uma instância carregada do contrato ContractVoting.
     */
    @Bean
    @Qualifier("votingContract")
    public ContractVoting votingContract(@Qualifier("web3jConnection") Web3j web3j,
            @Qualifier("credentialsContract") Credentials credentials,
            ContractGasProvider contractGasProvider) {
        // Carrega o contrato no endereço especificado usando as dependências
        // fornecidas.
        return ContractVoting.load(contractAddress, web3j, credentials, contractGasProvider);
    }
}