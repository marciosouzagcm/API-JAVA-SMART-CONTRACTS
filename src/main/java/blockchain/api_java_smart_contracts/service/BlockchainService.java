package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.crypto.Credentials; // <-- ADICIONADA ESSA IMPORTAÇÃO

import com.votacao.contracts.ContractVoting;

@Service
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    @Autowired
    @Qualifier("votingContract")
    private ContractVoting votingContract;

    @Autowired
    @Qualifier("web3jConnection")
    private Web3j web3j;

    @Autowired
    @Qualifier("credentialsContract")
    private Credentials adminCredentials; // AQUI ESTÁ SENDO USADA

    @Autowired
    private ContractGasProvider gasProvider;

    @Value("${blockchain.voting.contract-address}")
    private String contractAddress;

    @PostConstruct
    public void init() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            logger.info("Conectado ao nó Ethereum - Versão: {}", clientVersion);
        } catch (Exception e) {
            logger.error("Erro ao conectar ao nó Ethereum: {}", e.getMessage(), e);
        }
    }

    public CompletableFuture<TransactionReceipt> votar(BigInteger candidatoId, String eleitorAddress, BigInteger nonce, byte[] signedMessageHash, byte[] signature) throws BlockchainServiceException {
        try {
            return votingContract.votar(candidatoId, eleitorAddress, nonce, signedMessageHash, signature).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'votar' no contrato para candidato ID {}: {}", candidatoId, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao votar no contrato", e);
        }
    }

    public CompletableFuture<BigInteger> obterVotosDeCandidato(BigInteger candidatoDbId) throws BlockchainServiceException {
        try {
            return votingContract.getCandidateVotes(candidatoDbId).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao obter votos do candidato ID {} no contrato: {}", candidatoDbId, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter votos do candidato no contrato", e);
        }
    }

    public CompletableFuture<Boolean> verificarSeVotou(String enderecoEleitor) throws BlockchainServiceException {
        try {
            return votingContract.verificarSeVotou(enderecoEleitor).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'verificarSeVotou' no contrato para {}: {}", enderecoEleitor, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao verificar se eleitor votou no contrato", e);
        }
    }

    public CompletableFuture<BigInteger> obterTotalDeCandidatos() throws BlockchainServiceException {
        try {
            return votingContract.obterTotalDeCandidatos().sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'obterTotalDeCandidatos' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter total de candidatos do contrato", e);
        }
    }

    public CompletableFuture<String> obterTrustedSigner() throws BlockchainServiceException {
        try {
            return votingContract.trustedSigner().sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'trustedSigner' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter Trusted Signer do contrato", e);
        }
    }

    public String getDeployedContractAddress() {
        return contractAddress;
    }

    public CompletableFuture<String> deployContract() {
        return CompletableFuture.completedFuture("0xENDERECO_FICTICIO");
    }

    public int getTotalVotos(String contractAddress) {
        logger.warn("O método 'getTotalVotos' no BlockchainService não está implementado para refletir a lógica atual do contrato.");
        return 0;
    }

    public ContractVoting loadContract(String contractAddress) {
        if (web3j != null && adminCredentials != null && gasProvider != null) {
            return ContractVoting.load(contractAddress, web3j, adminCredentials, gasProvider);
        } else {
            logger.error("Não foi possível carregar o contrato: Web3j, Credenciais ou GasProvider são nulos.");
            return null;
        }
    }

    public String getConnectionStatus() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            return "Conectado ao nó Ethereum - Versão: " + clientVersion;
        } catch (Exception e) {
            return "Erro ao conectar: " + e.getMessage();
        }
    }
}