package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.ContractGasProvider; // Importa a interface correta

import com.votacao.api.exception.EleitorException;
import com.votacao.contracts.Voting; // Importação da classe Voting gerada pelo Web3j

import blockchain.api_java_smart_contracts.model.Candidato;

@Service
@SuppressWarnings("UnnecessaryReturnStatement") // Considerar remover esta supressão se não houver retornos desnecessários
public class VotingService {

    // Campos necessários para interações e carregamento dinâmico
    private final Web3j web3j; // Para operações gerais do Web3j (ex: verificar versão, estimar gás)
    private final String contractAddress; // Endereço do contrato para carregamentos dinâmicos
    private final ContractGasProvider gasProvider; // Provedor de gás (interface) para carregamentos dinâmicos
    private final Credentials adminCredentials; // Credenciais do administrador, usadas para operações que precisam de 'fromAddress'
    private final Voting votingContract; // A instância do contrato Voting para operações do administrador (já configurada via Spring)

    @Autowired
    private JdbcTemplate jdbcTemplate; // Para interações com o banco de dados

    private static final Logger LOGGER = Logger.getLogger(VotingService.class.getName());

    @Autowired
    public VotingService(
            // Injeção da conexão Web3j (qualificador 'web3jConnection' da BlockchainConfig)
            @Qualifier("web3jConnection") Web3j web3jConnection,
            // Injeção do endereço do contrato (valor do application.properties)
            @Value("${blockchain.voting.contract-address}") String contractAddressValue,
            // Injeção das credenciais do administrador (qualificador 'credentialsContract' da BlockchainConfig)
            @Qualifier("credentialsContract") Credentials credentialsContract,
            // Injeção do provedor de gás (bean ContractGasProvider da BlockchainConfig)
            ContractGasProvider injectedGasProvider, // O Spring pode injetar DefaultGasProvider se ele for o único ContractGasProvider
            // Injeção da instância do contrato Voting já carregada e configurada (qualificador 'votingContract' da BlockchainConfig)
            @Qualifier("votingContract") Voting adminVotingContract) {

        this.web3j = web3jConnection;
        this.contractAddress = contractAddressValue;
        this.adminCredentials = credentialsContract; // Campo para as credenciais do administrador
        this.gasProvider = injectedGasProvider; // Campo para o provedor de gás
        this.votingContract = adminVotingContract; // Campo para a instância do contrato Voting

        // Não é mais necessário carregar o contrato aqui, pois ele já é injetado.
        // this.votingContract = Voting.load(contractAddress, web3j, credentials, gasProvider);
    }

    /**
     * Chama a função `adicionarCandidato` do smart contract `Voting`.
     */
    public String adicionarCandidato(String nome) throws Exception {
        // A estimativa de gás é útil para pré-verificar custos.
        // 'adminCredentials.getAddress()' é usado para o campo 'from' da transação de estimativa.
        Function function = new Function(
                "adicionarCandidato", // Nome da função Solidity (minúscula, conforme o gerado)
                Arrays.asList(new Utf8String(nome)),
                Collections.emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        Transaction ethCallTransaction = Transaction.createEthCallTransaction(
                adminCredentials.getAddress(), // Usamos as credenciais do administrador para o "from"
                contractAddress,
                encodedFunction);
        EthEstimateGas estimateGas = web3j.ethEstimateGas(ethCallTransaction).sendAsync().get();

        if (estimateGas.hasError()) {
            throw new Exception("Erro ao estimar o gas: " + estimateGas.getError().getMessage());
        }
        BigInteger estimatedGas = estimateGas.getAmountUsed();
        LOGGER.log(Level.INFO, "Gás estimado para adicionar o candidato '" + nome + "': " + estimatedGas);

        // Executa a transação com o método gerado (adicionarCandidato) da instância do contrato injetada.
        TransactionReceipt receipt = votingContract.adicionarCandidato(nome).send();
        return receipt.getTransactionHash();
    }

    /**
     * Chama a função `autorizarEleitor` do smart contract `Voting` para autorizar um eleitor.
     */
    public String autorizarEleitor(String eleitorAddress) throws Exception {
        // Usa o método gerado: autorizarEleitor
        TransactionReceipt receipt = votingContract.autorizarEleitor(eleitorAddress, true).send();
        return receipt.getTransactionHash();
    }

    /**
     * Chama a função `votar` do smart contract `Voting` usando as credenciais padrão do administrador.
     */
    public String votar(Long idCandidato) throws Exception {
        // Usa o método gerado: votar
        TransactionReceipt receipt = votingContract.votar(BigInteger.valueOf(idCandidato)).send();
        return receipt.getTransactionHash();
    }

    /**
     * Chama a função `votar` do smart contract `Voting` usando credenciais específicas de um eleitor.
     */
    public String votar(Long idCandidato, String eleitorPrivateKey) throws Exception {
        // Cria novas credenciais para o eleitor específico
        Credentials eleitorCredentials = Credentials.create(eleitorPrivateKey);
        // Carrega uma NOVA instância do contrato Voting com as credenciais do eleitor e o provedor de gás injetado.
        Voting eleitorVotingContract = Voting.load(contractAddress, web3j, eleitorCredentials, gasProvider);
        
        // Agora, o método 'send()' retorna diretamente TransactionReceipt, não Object.
        TransactionReceipt receipt = eleitorVotingContract.votar(BigInteger.valueOf(idCandidato)).send();
        return receipt.getTransactionHash();
    }

    /**
     * Chama a função `obterCandidato` do smart contract `Voting` para obter informações de um candidato.
     */
    public Candidato obterCandidato(Long id) {
        try {
            // Usa o método gerado: obterCandidato
            Tuple3<BigInteger, String, BigInteger> result = votingContract.obterCandidato(BigInteger.valueOf(id)).send();
            BigInteger candidatoId = result.getValue1();
            String nome = result.getValue2();
            BigInteger contagemDeVotos = result.getValue3();
            return new Candidato(candidatoId, nome, contagemDeVotos);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter informações do candidato", e);
            throw new RuntimeException("Erro ao obter informações do candidato: " + e.getMessage(), e);
        }
    }

    /**
     * Método para autorizar um eleitor, verificando suas credenciais em um banco de dados.
     */
    public String autorizarEleitorComCredenciais(String endereco, String usuario, String senha)
            throws EleitorException {
        if (verificarCredenciais(usuario, senha)) {
            try {
                return autorizarEleitor(endereco); // Este método já chama o contrato Voting
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao autorizar o eleitor", e);
                throw new RuntimeException("Erro ao autorizar o eleitor: " + e.getMessage(), e);
            }
        } else {
            throw new EleitorException("Credenciais inválidas.");
        }
    }

    /**
     * Verifica as credenciais de um eleitor em um banco de dados usando JdbcTemplate.
     */
    private boolean verificarCredenciais(String usuario, String senha) {
        String sql = "SELECT COUNT(*) FROM autorizarEleitor WHERE login = ? AND senha = ?";
        // Usando queryForObject para um único resultado. Retorna null se não houver resultado, ou o valor Integer.
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuario, senha);
        return count != null && count > 0;
    }

    /**
     * Verifica o status da conexão com o nó Ethereum.
     */
    public String verificarStatusContrato() {
        try {
            // Usa o 'web3j' injetado, que já está configurado.
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            return "Conectado ao nó Ethereum: " + clientVersion.getWeb3ClientVersion();
        } catch (Exception e) {
            return "Erro ao conectar ao nó Ethereum: " + e.getMessage();
        }
    }
}