package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blockchain.api_java_smart_contracts.model.Candidato;

@Service // Indica que esta classe é um serviço Spring, contendo lógica de negócios relacionados a candidatos
public class CandidatoService {

    private static final Logger LOGGER = Logger.getLogger(CandidatoService.class.getName()); // Cria um logger para registrar informações sobre o serviço de candidatos

    @Autowired // Injeta uma instância do serviço BlockchainService para interagir com a blockchain
    private BlockchainService blockchainService;

    // Método para obter as informações de um candidato com base no seu ID
    public CompletableFuture<Candidato> obterCandidato(Long id) throws CandidatoServiceException, BlockchainServiceException {
        LOGGER.log(Level.INFO, "Obtendo informações do candidato com ID: {0}", id); // Registra o início da operação de obtenção do candidato
        return blockchainService.obterCandidato(BigInteger.valueOf(id)) // Chama o serviço de blockchain para obter o candidato pelo ID (convertido para BigInteger)
                .thenApply(candidato -> { // Executa esta função assim que o resultado da chamada ao blockchainService estiver disponível
                    if (candidato != null) { // Verifica se um candidato foi encontrado com o ID fornecido
                        method(candidato); // Chama um método interno para logar as informações do candidato obtido
                        return candidato; // Retorna o objeto Candidato obtido
                    } else {
                        LOGGER.log(Level.WARNING, "Candidato não encontrado com ID: {0}", id); // Registra um aviso se nenhum candidato for encontrado
                        return null; // Retorna null indicando que o candidato não foi encontrado
                    }
                })
                .exceptionally(e -> { // Executa esta função se ocorrer alguma exceção durante a chamada ao blockchainService ou no bloco thenApply
                    LOGGER.log(Level.SEVERE, String.format("Erro ao obter candidato com ID %d: %s", id, e.getMessage()), e); // Registra um erro grave com a mensagem da exceção
                    try {
                        throw new CandidatoServiceException("Erro ao obter candidato: " + e.getMessage(), e); // Lança uma exceção de serviço de candidato personalizada para encapsular o erro
                    } catch (CandidatoServiceException ex) {
                        // Log da exceção capturada para evitar perda de informação
                        LOGGER.log(Level.SEVERE, "Erro ao lançar CandidatoServiceException", ex);
                    }
                    return null; // Retorna null em caso de erro
                });
    }

    // Método privado para logar as informações do candidato obtido
    private void method(Candidato candidato) {
        LOGGER.log(Level.INFO, "Candidato obtido: ID={0}, Nome={1}", new Object[]{candidato.getId(), candidato.getNome()}); // Registra as informações do candidato
    }
}