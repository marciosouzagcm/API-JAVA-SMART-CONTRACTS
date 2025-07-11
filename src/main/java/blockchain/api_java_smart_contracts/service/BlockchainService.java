package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider; // Importe esta classe, se ainda não estiver

import com.votacao.contracts.ContractVoting; // Certifique-se de que este é o pacote correto gerado pelo web3j

import jakarta.annotation.PostConstruct;

@Service
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    // Removido @Autowired para ContractVoting aqui, pois será gerenciado dinamicamente
    // private ContractVoting votingContract; // COMENTADO/REMOVIDO

    @Autowired
    @Qualifier("web3jConnection")
    private Web3j web3j;

    @Autowired
    @Qualifier("credentialsContract")
    private Credentials adminCredentials;

    @Autowired
    private ContractGasProvider gasProvider; // Injetado via Spring (geralmente DefaultGasProvider)

    @Value("${blockchain.voting.contract-address}")
    private String contractAddressConfig; // Renomeado para evitar conflito com o campo de instância

    // Campo para armazenar a instância do contrato após deploy/carregamento
    private ContractVoting currentVotingContractInstance;

    @PostConstruct
    public void init() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            logger.info("Conectado ao nó Ethereum - Versão: {}", clientVersion);
        } catch (Exception e) {
            logger.error("Erro ao conectar ao nó Ethereum: {}", e.getMessage(), e);
        }
    }

    /**
     * Realiza uma votação no contrato inteligente.
     *
     * @param candidatoId O ID do candidato.
     * @param eleitorAddress O endereço do eleitor.
     * @param nonce O nonce da transação.
     * @param signedMessageHash O hash da mensagem assinada.
     * @param signature A assinatura da mensagem.
     * @return Um CompletableFuture contendo o recibo da transação.
     * @throws BlockchainServiceException Se ocorrer um erro ao votar.
     */
    public CompletableFuture<TransactionReceipt> votar(BigInteger candidatoId, String eleitorAddress, BigInteger nonce,
            byte[] signedMessageHash, byte[] signature) throws BlockchainServiceException {
        try {
            // Usa a instância carregada/deployada do contrato
            if (currentVotingContractInstance == null) {
                throw new IllegalStateException("Contrato Voting não está inicializado. Verifique o deploy/carregamento.");
            }
            return currentVotingContractInstance.votar(candidatoId, eleitorAddress, nonce, signedMessageHash, signature).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'votar' no contrato para candidato ID {}: {}", candidatoId, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao votar no contrato", e);
        }
    }

    /**
     * Obtém o número de votos de um candidato específico.
     *
     * @param candidatoDbId O ID do candidato no banco de dados (que corresponde ao ID no contrato).
     * @return Um CompletableFuture contendo o número de votos.
     * @throws BlockchainServiceException Se ocorrer um erro ao obter os votos.
     */
    public CompletableFuture<BigInteger> obterVotosDeCandidato(BigInteger candidatoDbId)
            throws BlockchainServiceException {
        try {
            if (currentVotingContractInstance == null) {
                throw new IllegalStateException("Contrato Voting não está inicializado. Verifique o deploy/carregamento.");
            }
            return currentVotingContractInstance.getCandidateVotes(candidatoDbId).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao obter votos do candidato ID {} no contrato: {}", candidatoDbId, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter votos do candidato no contrato", e);
        }
    }

    /**
     * Verifica se um eleitor já votou.
     *
     * @param enderecoEleitor O endereço Ethereum do eleitor.
     * @return Um CompletableFuture contendo um booleano indicando se o eleitor votou.
     * @throws BlockchainServiceException Se ocorrer um erro ao verificar o status da votação.
     */
    public CompletableFuture<Boolean> verificarSeVotou(String enderecoEleitor) throws BlockchainServiceException {
        try {
            if (currentVotingContractInstance == null) {
                throw new IllegalStateException("Contrato Voting não está inicializado. Verifique o deploy/carregamento.");
            }
            return currentVotingContractInstance.verificarSeVotou(enderecoEleitor).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'verificarSeVotou' no contrato para {}: {}", enderecoEleitor, e.getMessage(),
                    e);
            throw new BlockchainServiceException("Erro ao verificar se eleitor votou no contrato", e);
        }
    }

    /**
     * Obtém o endereço do signatário confiável (trusted signer) do contrato.
     *
     * @return Um CompletableFuture contendo o endereço do trusted signer.
     * @throws BlockchainServiceException Se ocorrer um erro ao obter o trusted signer.
     */
    public CompletableFuture<String> obterTrustedSigner() throws BlockchainServiceException {
        try {
            if (currentVotingContractInstance == null) {
                throw new IllegalStateException("Contrato Voting não está inicializado. Verifique o deploy/carregamento.");
            }
            return currentVotingContractInstance.trustedSigner().sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'trustedSigner' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter Trusted Signer do contrato", e);
        }
    }

    /**
     * Obtém o total de votos registrados no contrato.
     *
     * @return Um CompletableFuture contendo o total de votos.
     * @throws BlockchainServiceException Se ocorrer um erro ao obter o total de votos.
     */
    public CompletableFuture<BigInteger> getTotalVotos() throws BlockchainServiceException {
        try {
            if (currentVotingContractInstance == null) {
                throw new IllegalStateException("Contrato Voting não está inicializado. Verifique o deploy/carregamento.");
            }
            // Supondo que o seu contrato VotingContract tenha um método chamado 'getTotalVotesCount()'
            // que retorna um BigInteger (correspondente a um 'uint' no Solidity).
            return currentVotingContractInstance.getTotalVotesCount().sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao obter o total de votos do contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter o total de votos do contrato", e);
        }
    }

    /**
     * Realiza o deploy do contrato Voting na blockchain.
     *
     * @return Um CompletableFuture contendo o endereço do contrato deployado.
     * @throws RuntimeException Se ocorrer um erro durante o deploy.
     */
    public CompletableFuture<String> deployContractAndReturnAddress() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Iniciando o deploy do contrato Voting...");
                // Realiza o deploy do contrato usando as credenciais do admin e o gasProvider.
                // O método .send() executa a transação de deploy e aguarda o recibo.
                ContractVoting deployedContract = ContractVoting.deploy(web3j, adminCredentials, gasProvider).send();

                if (deployedContract.isValid()) {
                    String deployedAddress = deployedContract.getContractAddress();
                    logger.info("Contrato Voting deployado com sucesso no endereço: {}", deployedAddress);
                    this.currentVotingContractInstance = deployedContract; // Armazena a instância deployada
                    return deployedAddress;
                } else {
                    String errorMessage = "Falha no deploy do contrato Voting. Contrato inválido após deploy.";
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
            } catch (Exception e) {
                logger.error("Erro fatal durante o deploy do contrato Voting: {}", e.getMessage(), e);
                throw new RuntimeException("Erro ao deployar o contrato Voting", e);
            }
        });
    }

    /**
     * Carrega uma instância do contrato Voting a partir de um endereço existente na blockchain.
     *
     * @param contractAddress O endereço do contrato a ser carregado.
     * @return A instância do ContractVoting carregada.
     */
    public ContractVoting loadContract(String contractAddress) {
        if (web3j != null && adminCredentials != null && gasProvider != null) {
            ContractVoting loadedContract = ContractVoting.load(contractAddress, web3j, adminCredentials, gasProvider);
            this.currentVotingContractInstance = loadedContract; // Armazena a instância carregada
            logger.info("Contrato Voting carregado do endereço: {}", contractAddress);
            return loadedContract;
        } else {
            logger.error("Não foi possível carregar o contrato: Web3j, Credenciais ou GasProvider são nulos.");
            return null;
        }
    }

    /**
     * Define a instância atual do contrato Voting.
     * Usado internamente ou pela CommandLineRunner para setar o contrato deployado/carregado.
     *
     * @param contract A instância do ContractVoting.
     */
    public void setCurrentVotingContractInstance(ContractVoting contract) {
        this.currentVotingContractInstance = contract;
    }

    /**
     * Retorna o endereço da instância do contrato atualmente em uso.
     *
     * @return O endereço do contrato.
     */
    public String getDeployedContractAddress() {
        // Retorna o endereço da instância atualmente em uso, que pode ser a deployada ou a carregada.
        return (currentVotingContractInstance != null) ? currentVotingContractInstance.getContractAddress() : this.contractAddressConfig;
    }

    /**
     * Retorna o status da conexão com o nó Ethereum.
     *
     * @return Uma string descrevendo o status da conexão.
     */
    public String getConnectionStatus() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            return "Conectado ao nó Ethereum - Versão: " + clientVersion;
        } catch (Exception e) {
            return "Erro ao conectar: " + e.getMessage();
        }
    }
}