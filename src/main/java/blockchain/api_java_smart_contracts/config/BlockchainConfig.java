package blockchain.api_java_smart_contracts.config;

import java.math.BigInteger; // Import necessário se você for usar BigInteger explicitamente para valores de gás, etc.

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary; // Import opcional, útil se precisar de um bean padrão

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.ContractGasProvider;

// IMPORTAÇÃO CORRIGIDA: Importa a classe gerada pelo Web3j
import com.votacao.contracts.ContractVoting;

@Configuration // Esta anotação é CRÍTICA: informa ao Spring que esta classe define beans
public class BlockchainConfig {

    // Injeta a URL do nó Ethereum do application.properties (nome da propriedade corrigido)
    @Value("${blockchain.ethereum.node-url}")
    private String nodeUrl;

    // Injeta a chave privada do administrador do application.properties (nome da propriedade corrigido)
    @Value("${blockchain.admin.private-key}")
    private String adminPrivateKey;

    // Injeta o endereço do contrato Voting do application.properties
    @Value("${blockchain.voting.contract-address}")
    private String contractAddress;

    /**
     * Define o bean Web3j principal.
     * O @Primary é útil quando você tem múltiplos beans do mesmo tipo,
     * e este é o preferencial por padrão.
     * O @Qualifier("web3jPrincipal") é o qualificador que seu erro indicou que faltava,
     * garantindo que esta instância seja injetada onde especificado.
     */
    @Bean
    @Primary // Indica que este é o bean preferencial para injeções de Web3j sem qualificador específico
    @Qualifier("web3jPrincipal") // Qualificador explícito, caso sua aplicação principal o use
    public Web3j web3jPrincipal() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define um bean Web3j para conexão geral, se necessário (ex: em um serviço que interage amplamente).
     */
    @Bean
    @Qualifier("web3jConnection")
    public Web3j web3jConnection() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define um bean Web3j específico para o contrato, se houver lógica separada que precise dele.
     */
    @Bean
    @Qualifier("contractWeb3j")
    public Web3j contractWeb3j() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    /**
     * Define o bean de credenciais para o administrador do contrato, usando a chave privada.
     */
    @Bean
    @Qualifier("credentialsContract")
    public Credentials credentialsContract() {
        return Credentials.create(adminPrivateKey);
    }

    /**
     * Define o provedor de gas para as transações do contrato.
     * DefaultGasProvider usa valores padrão para gasPrice e gasLimit.
     */
    @Bean
    public ContractGasProvider contractGasProvider() {
        // Para personalizar os preços de gas, você pode criar uma implementação customizada
        // ou instanciar DefaultGasProvider com valores específicos:
        // return new DefaultGasProvider(BigInteger.valueOf(20_000_000_000L), BigInteger.valueOf(6_700_000));
        return new DefaultGasProvider();
    }

    /**
     * Define o bean do contrato ContractVoting.
     * Ele injeta as dependências Web3j, Credentials e GasProvider necessárias para interagir com o contrato.
     * É crucial que 'ContractVoting.load' seja chamado na classe gerada pelo Web3j.
     */
    @Bean
    @Qualifier("votingContract")
    // TIPO DE RETORNO CORRIGIDO: De Voting para ContractVoting
    public ContractVoting votingContract(@Qualifier("web3jConnection") Web3j web3j, // Injeta o Web3j com o qualificador correto
                                         @Qualifier("credentialsContract") Credentials credentials, // Injeta as credenciais
                                         ContractGasProvider contractGasProvider) { // Injeta o provedor de gas
        // CHAMADA DO MÉTODO LOAD CORRIGIDA: De Voting.load para ContractVoting.load
        return ContractVoting.load(contractAddress, web3j, credentials, contractGasProvider);
    }
}