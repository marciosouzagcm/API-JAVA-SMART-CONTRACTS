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
import blockchain.api_java_smart_contracts.service.VotingService.VotoCandidatoDTO;
import blockchain.api_java_smart_contracts.model.dto.VotoRequest; // Certifique-se de que este DTO está com os campos removidos.

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/voting")
public class VotingController {

    private static final Logger LOGGER = Logger.getLogger(VotingController.class.getName());

    @Autowired
    private VotingService votingService;

    /**
     * Endpoint para votar em um candidato. Sua API gera a assinatura.
     * Este endpoint recebe um objeto VotoRequest que contém o ID do candidato,
     * o endereço do eleitor e um nonce.
     *
     * Acesse com POST para http://localhost:8080/api/voting/vote
     *
     * O corpo da requisição deve ser um JSON:
     * {
     * "idCandidato": 1,
     * "voterAddress": "0xSEU_ENDERECO_ELEITOR",
     * "nonce": 123 // Opcional, dependendo de quem gera o nonce (cliente ou API)
     * }
     *
     * @param voteRequest Objeto VotoRequest contendo idCandidato, voterAddress e nonce.
     * @return ResponseEntity com o hash da transação em caso de sucesso, ou uma mensagem de erro.
     */
    @PostMapping("/vote")
    public ResponseEntity<String> vote(@Valid @RequestBody VotoRequest voteRequest) {
        try {
            LOGGER.log(Level.INFO, "Requisição para votar no candidato de ID: {0} pelo eleitor: {1} com nonce: {2}",
                    new Object[]{voteRequest.getIdCandidato(), voteRequest.getVoterAddress(), voteRequest.getNonce()});

            // Chama o novo método no VotingService que AGORA gera a assinatura internamente
            String transactionHash = votingService.registerVoteWithSignature(
                    BigInteger.valueOf(voteRequest.getIdCandidato()),
                    voteRequest.getVoterAddress(),
                    voteRequest.getNonce() // Passa o nonce para o serviço
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

    // --- Outros Endpoints (mantidos como estão) ---
    // getVotes, getAllCandidatesVotes, hasVoted, hexStringToByteArray
    // ...
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