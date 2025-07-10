package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blockchain.api_java_smart_contracts.model.Candidato;
import blockchain.api_java_smart_contracts.model.dto.BlockchainCandidateDetails; // Mantenha esta importação, pois ela pode ser usada internamente pelo BlockchainService
import blockchain.api_java_smart_contracts.repository.CandidatoRepository;

@Service
public class CandidatoService {

    private static final Logger LOGGER = Logger.getLogger(CandidatoService.class.getName());

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private CandidatoRepository candidatoRepository;

    /**
     * Salva um novo candidato no banco de dados.
     * @param candidato O objeto Candidato a ser salvo.
     * @return O Candidato salvo com o ID gerado.
     */
    public Candidato salvarCandidato(Candidato candidato) {
        LOGGER.log(Level.INFO, "Salvando candidato no banco de dados: {0}", candidato.getNome());
        return candidatoRepository.save(candidato);
    }

    /**
     * Busca um candidato no banco de dados pelo seu ID.
     * @param id O ID do candidato no banco de dados.
     * @return Um Optional contendo o Candidato, se encontrado.
     */
    public Optional<Candidato> buscarCandidatoPorId(Long id) {
        LOGGER.log(Level.INFO, "Buscando candidato no banco de dados com ID: {0}", id);
        return candidatoRepository.findById(id);
    }

    /**
     * Retorna todos os candidatos do banco de dados.
     * @return Uma lista de todos os candidatos.
     */
    public List<Candidato> buscarTodosCandidatos() {
        LOGGER.log(Level.INFO, "Buscando todos os candidatos no banco de dados.");
        return candidatoRepository.findAll();
    }

    /**
     * Deleta um candidato do banco de dados pelo seu ID.
     * @param id O ID do candidato a ser deletado.
     */
    public void deletarCandidato(Long id) {
        LOGGER.log(Level.INFO, "Deletando candidato do banco de dados com ID: {0}", id);
        candidatoRepository.deleteById(id);
    }

    /**
     * Obtém as informações de um candidato, buscando primeiro no banco de dados
     * e depois complementando com os votos da blockchain.
     *
     * @param id O ID do candidato no banco de dados (Long).
     * @return Um CompletableFuture contendo o objeto Candidato com votos da blockchain.
     * @throws CandidatoServiceException Se ocorrer um erro no serviço de candidato.
     * @throws BlockchainServiceException Se ocorrer um erro na interação com a blockchain.
     */
    public CompletableFuture<Candidato> obterCandidatoComVotosBlockchain(Long id)
            throws CandidatoServiceException, BlockchainServiceException {
        LOGGER.log(Level.INFO, "Obtendo informações do candidato com ID: {0}", id);

        // 1. Busca o candidato no banco de dados
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);

        if (candidatoOptional.isEmpty()) {
            LOGGER.log(Level.WARNING, "Candidato não encontrado no DB com ID: {0}", id);
            // Lança uma exceção se o candidato não for encontrado no DB
            throw new CandidatoServiceException("Candidato não encontrado no banco de dados com ID: " + id);
        }

        Candidato candidatoDB = candidatoOptional.get();

        // 2. Busca os detalhes do candidato na blockchain usando o ID do DB (convertido para BigInteger)
        //    Assumimos que o ID do DB corresponde ao ID do contrato na blockchain.
        // ALTERAÇÃO: 'obterCandidato' foi substituído por 'obterVotosDeCandidato'
        return blockchainService.obterVotosDeCandidato(BigInteger.valueOf(id))
                .thenApply(blockchainDetails -> {
                    // Mapeia os votos da blockchain para o objeto Candidato do DB
                    // CORREÇÃO: blockchainDetails já é o BigInteger de votos, não precisa de .getVotes()
                    candidatoDB.setVotosBlockchain(blockchainDetails); // Linha corrigida
                    LOGGER.log(Level.INFO, "Candidato obtido e votos da blockchain combinados: ID={0}, Nome={1}, Votos={2}",
                            new Object[]{candidatoDB.getId(), candidatoDB.getNome(), candidatoDB.getVotosBlockchain()});
                    return candidatoDB;
                })
                .exceptionally(e -> {
                    LOGGER.log(Level.SEVERE, String.format("Erro ao obter votos da blockchain para candidato ID %d: %s", id, e.getMessage()), e);
                    throw new RuntimeException(new CandidatoServiceException("Erro ao obter votos da blockchain: " + e.getMessage(), e));
                });
    }
}