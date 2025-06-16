package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service; // Importe a anotação Service
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.votacao.contracts.Voting; // Importação correta para a classe gerada

import blockchain.api_java_smart_contracts.model.Candidato;
import jakarta.annotation.PostConstruct;

@Service // Adicione esta anotação aqui!
public class BlockchainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

    @Autowired
    @Qualifier("votingContract")
    private Voting voting; // Voting é a classe de contrato gerada pelo web3j

    @Autowired
    @Qualifier("web3jConnection")
    private Web3j web3j;

    @PostConstruct
    public void init() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            logger.info("Conectado ao nó Ethereum - Versão: {}", clientVersion);
        } catch (Exception e) {
            logger.error("Erro ao conectar ao nó Ethereum: {}", e.getMessage(), e);
        }
    }

    public CompletableFuture<TransactionReceipt> adicionarCandidato(String nome) throws BlockchainServiceException {
        try {
            // Nome do método ajustado: adicionarCandidato
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return voting.adicionarCandidato(nome).send();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            logger.error("Erro ao chamar 'adicionarCandidato' no contrato para nome {}: {}", nome, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao adicionar candidato no contrato", e);
        }
    }

    public CompletableFuture<TransactionReceipt> autorizarEleitor(String enderecoEleitor, boolean autorizado)
            throws BlockchainServiceException {
        try {
            // Nome do método ajustado: autorizarEleitor
            return voting.autorizarEleitor(enderecoEleitor, autorizado).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'autorizarEleitor' no contrato para {}: {}", enderecoEleitor, e.getMessage(),
                    e);
            throw new BlockchainServiceException("Erro ao autorizar/desautorizar eleitor no contrato", e);
        }
    }

    public CompletableFuture<TransactionReceipt> votar(BigInteger candidatoId) throws BlockchainServiceException {
        try {
            // Nome do método ajustado: votar
            return voting.votar(candidatoId).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'votar' no contrato para candidato ID {}: {}", candidatoId, e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao votar no contrato", e);
        }
    }

    public CompletableFuture<Candidato> obterCandidato(BigInteger id) throws BlockchainServiceException {
        try {
            // Nome do método ajustado: obterCandidato
            return voting.obterCandidato(id).sendAsync()
                    .thenApply(web3jCandidato -> new Candidato(
                            web3jCandidato.component1(),
                            web3jCandidato.component2(),
                            web3jCandidato.component3()))
                    .exceptionally(e -> {
                        logger.error("Erro ao obter candidato do contrato para ID {}: {}", id, e.getMessage(), e);
                        throw new RuntimeException(
                                new BlockchainServiceException("Erro ao obter candidato do contrato", e));
                    });
        } catch (Exception e) {
            logger.error("Erro ao iniciar a chamada de 'obterCandidato' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao iniciar a operação de obter candidato", e);
        }
    }

    public CompletableFuture<Boolean> verificarSeVotou(String enderecoEleitor) throws BlockchainServiceException {
        try {
            // Nome do método ajustado: verificarSeVotou
            return voting.verificarSeVotou(enderecoEleitor).sendAsync();
        } catch (Exception e) {
            logger.error("Erro ao chamar 'verificarSeVotou' no contrato para {}: {}", enderecoEleitor, e.getMessage(),
                    e);
            throw new BlockchainServiceException("Erro ao verificar se eleitor votou no contrato", e);
        }
    }

    public CompletableFuture<BigInteger> obterTotalDeCandidatos() throws BlockchainServiceException {
        try {
            // Nome do método ajustado: contadorDeCandidatos
            return voting.contadorDeCandidatos().sendAsync(); // Ajustado aqui
        } catch (Exception e) {
            logger.error("Erro ao chamar 'obterTotalDeCandidatos' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter total de candidatos do contrato", e);
        }
    }

    public CompletableFuture<String> obterAdministrador() throws BlockchainServiceException {
        try {
            // Nome do método ajustado: administrador
            return voting.administrador().sendAsync(); // Ajustado aqui
        } catch (Exception e) {
            logger.error("Erro ao chamar 'administrador' no contrato: {}", e.getMessage(), e);
            throw new BlockchainServiceException("Erro ao obter administrador do contrato", e);
        }
    }
}