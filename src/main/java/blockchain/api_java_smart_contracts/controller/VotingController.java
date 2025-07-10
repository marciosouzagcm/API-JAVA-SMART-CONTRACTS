package blockchain.api_java_smart_contracts.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blockchain.api_java_smart_contracts.service.VotingService;
import blockchain.api_java_smart_contracts.service.VotingService.VotoCandidatoDTO; // Importe o DTO interno
import blockchain.api_java_smart_contracts.model.dto.VotoRequest; // Importa a classe VotoRequest do pacote model.dto

import jakarta.validation.Valid; // Para habilitar as validações no @RequestBody

@RestController
@RequestMapping("/api/voting")
public class VotingController {

    private static final Logger LOGGER = Logger.getLogger(VotingController.class.getName());

    @Autowired
    private VotingService votingService;

    // --- ENDPOINTS REMOVIDOS DEVIDO ÀS MUDANÇAS NO CONTRATO ---
    // A funcionalidade de adicionar candidatos diretamente no contrato foi removida.
    // Candidatos agora são gerenciados off-chain (ex: em um banco de dados).
    // O endpoint '/addCandidate' e '/candidates' (para nomes) foram removidos
    // pois o contrato não os suporta mais.

    /**
     * Endpoint para votar em um candidato, incluindo os dados de assinatura.
     * Este endpoint recebe um objeto VotoRequest que contém o ID do candidato,
     * o endereço do eleitor, um nonce, o hash da mensagem assinada e a assinatura.
     *
     * Acesse com POST para http://localhost:8080/api/voting/vote
     *
     * O corpo da requisição deve ser um JSON:
     * {
     * "idCandidato": 1,
     * "voterAddress": "0xSEU_ENDERECO_ELEITOR",
     * "nonce": 123,
     * "signedMessageHash": "0xHASH_DA_MENSAGEM_ASSINADA",
     * "signature": "0xASSINATURA_DIGITAL_GERADA"
     * }
     *
     * A lógica para gerar 'signedMessageHash' e 'signature' deve ser implementada
     * no cliente ou em um serviço intermediário que tenha acesso à chave privada do 'trustedSigner'.
     *
     * @param voteRequest Objeto VotoRequest contendo todos os dados necessários para o voto.
     * @return ResponseEntity com o hash da transação em caso de sucesso, ou uma mensagem de erro.
     */
    @PostMapping("/vote")
    public ResponseEntity<String> vote(@Valid @RequestBody VotoRequest voteRequest) {
        try {
            LOGGER.log(Level.INFO, "Requisição para votar no candidato de ID: {0} pelo eleitor: {1}",
                    new Object[]{voteRequest.getIdCandidato(), voteRequest.getVoterAddress()});

            // Converte strings hexadecimais (com ou sem "0x") para arrays de bytes
            byte[] signedMessageHashBytes = hexStringToByteArray(voteRequest.getSignedMessageHash());
            byte[] signatureBytes = hexStringToByteArray(voteRequest.getSignature());

            // Chama o serviço de votação, convertendo o ID do candidato de Long para BigInteger
            String transactionHash = votingService.votar(
                    BigInteger.valueOf(voteRequest.getIdCandidato()),
                    voteRequest.getVoterAddress(),
                    voteRequest.getNonce(),
                    signedMessageHashBytes,
                    signatureBytes
            );

            LOGGER.log(Level.INFO, "Voto registrado para o candidato de ID {0}. Hash da transação: {1}",
                    new Object[]{voteRequest.getIdCandidato(), transactionHash});
            return ResponseEntity.ok("Voto registrado. Hash da transação: " + transactionHash);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao votar no candidato de ID '{0}': {1}",
                    new Object[]{voteRequest.getIdCandidato(), e.getMessage()});
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao votar: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obter o número de votos de um candidato específico.
     * O ID do candidato aqui deve corresponder ao ID usado no seu banco de dados
     * para identificar o candidato, que é o mesmo ID usado na blockchain.
     *
     * Acesse com GET para http://localhost:8080/api/voting/votes/{candidateId}
     *
     * @param candidateId O ID do candidato (do seu DB/blockchain).
     * @return ResponseEntity com a contagem de votos do candidato.
     */
    @GetMapping("/votes/{candidateId}")
    public ResponseEntity<BigInteger> getVotes(@PathVariable BigInteger candidateId) {
        try {
            LOGGER.log(Level.INFO, "Requisição para obter votos do candidato de ID: {0}", candidateId);
            // Chama votingService.obterVotos(candidateId) para obter a contagem de votos
            BigInteger votes = votingService.obterVotos(candidateId);
            LOGGER.log(Level.INFO, "Votos para o candidato {0}: {1}", new Object[]{candidateId, votes});
            return ResponseEntity.ok(votes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter votos para o candidato '{0}': {1}",
                    new Object[]{candidateId, e.getMessage()});
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Endpoint para obter os votos de *todos* os candidatos conhecidos pelo seu sistema.
     * Esta função simula a obtenção de IDs de candidatos de um banco de dados e
     * então busca suas contagens de votos na blockchain.
     * Em uma aplicação real, os IDs dos candidatos viriam de uma consulta ao seu DB.
     *
     * Acesse com GET para http://localhost:8080/api/voting/allCandidatesVotes
     *
     * @return ResponseEntity com uma lista de DTOs contendo o ID do candidato e sua contagem de votos.
     */
    @GetMapping("/allCandidatesVotes")
    public ResponseEntity<List<VotoCandidatoDTO>> getAllCandidatesVotes() {
        try {
            LOGGER.log(Level.INFO, "Requisição para obter votos de todos os candidatos.");

            // SIMULAÇÃO: Em uma aplicação real, estes IDs viriam do seu banco de dados.
            // Exemplo: List<BigInteger> candidateIdsFromDB = yourCandidateService.getAllCandidateIds();
            List<BigInteger> candidateIdsFromDB = new ArrayList<>();
            candidateIdsFromDB.add(BigInteger.valueOf(1));
            candidateIdsFromDB.add(BigInteger.valueOf(2));
            candidateIdsFromDB.add(BigInteger.valueOf(3)); // Exemplo de IDs de candidatos

            List<VotoCandidatoDTO> allVotes = votingService.obterVotosDosCandidatosExistentes(candidateIdsFromDB);
            LOGGER.log(Level.INFO, "Votos de todos os candidatos obtidos.");
            return ResponseEntity.ok(allVotes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter votos de todos os candidatos: {0}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Endpoint para verificar se um eleitor específico já votou na blockchain.
     *
     * Acesse com GET para http://localhost:8080/api/voting/hasVoted/{voterAddress}
     *
     * @param voterAddress O endereço Ethereum do eleitor a ser verificado.
     * @return ResponseEntity com 'true' se o eleitor já votou, 'false' caso contrário.
     */
    @GetMapping("/hasVoted/{voterAddress}")
    public ResponseEntity<Boolean> hasVoted(@PathVariable String voterAddress) {
        try {
            LOGGER.log(Level.INFO, "Verificando se o eleitor {0} já votou.", voterAddress);
            boolean votou = votingService.verificarSeEleitorVotou(voterAddress);
            LOGGER.log(Level.INFO, "Eleitor {0} votou: {1}", new Object[]{voterAddress, votou});
            return ResponseEntity.ok(votou);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar status de voto para eleitor '{0}': {1}",
                    new Object[]{voterAddress, e.getMessage()});
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Método utilitário para converter uma string hexadecimal (com ou sem "0x")
     * para um array de bytes.
     *
     * @param hexString A string hexadecimal.
     * @return O array de bytes correspondente.
     */
    private byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
            return new byte[0];
        }
        String cleanHexString = hexString.startsWith("0x") ? hexString.substring(2) : hexString;
        // Se a string hexadecimal tiver um número ímpar de caracteres, adicione um '0' no início
        if (cleanHexString.length() % 2 != 0) {
            cleanHexString = "0" + cleanHexString;
        }
        int len = cleanHexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(cleanHexString.charAt(i), 16) << 4)
                                 + Character.digit(cleanHexString.charAt(i + 1), 16));
        }
        return data;
    }
}