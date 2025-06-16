package blockchain.api_java_smart_contracts.controller;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blockchain.api_java_smart_contracts.model.Candidato;
import blockchain.api_java_smart_contracts.model.dto.AdicionarCandidatoRequest;
import blockchain.api_java_smart_contracts.model.dto.AutorizarEleitorRequest;
import blockchain.api_java_smart_contracts.model.dto.EleitorResponse;
import blockchain.api_java_smart_contracts.model.dto.VotoRequest;
import blockchain.api_java_smart_contracts.service.BlockchainService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api") // Prefixo para todos os endpoints desta classe
public class CandidatoController {

    @Autowired
    private BlockchainService blockchainService;

    /**
     * Adiciona um novo candidato ao sistema de votação.
     * Recebe o nome do candidato no corpo da requisição via DTO.
     *
     * @param request DTO contendo o nome do candidato.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/candidatos")
    public ResponseEntity<String> adicionarCandidato(@Valid @RequestBody AdicionarCandidatoRequest request) {
        try {
            // Chama o método para adicionar candidato e aguarda a conclusão
            blockchainService.adicionarCandidato(request.getNome()).get();
            return ResponseEntity.ok("Candidato '" + request.getNome() + "' adicionado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar candidato: " + e.getMessage());
        }
    }

    /**
     * Obtém os detalhes de um candidato específico pelo seu ID.
     *
     * @param id ID do candidato.
     * @return ResponseEntity com o objeto Candidato ou status 404 Not Found.
     */
    @GetMapping("/candidatos/{id}")
    public ResponseEntity<?> obterCandidato(@PathVariable BigInteger id) {
        try {
            Candidato candidato = blockchainService.obterCandidato(id).get();
            // Verifica se o candidato realmente existe com base nos valores retornados do
            // contrato
            // (Ex: ID maior que zero e nome não vazio para considerar existente)
            if (candidato != null && candidato.getId().compareTo(BigInteger.ZERO) > 0
                    && !candidato.getNome().isEmpty()) {
                return ResponseEntity.ok(candidato);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Erro interno: Operação interrompida ao obter candidato.");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause(); // Tenta obter a causa real da exceção
            String errorMessage = "Erro ao obter candidato: " + (cause != null ? cause.getMessage() : e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro inesperado ao obter candidato: " + e.getMessage());
        }
    }

    /**
     * Registra um voto para um candidato.
     * Recebe o ID do candidato no corpo da requisição via DTO.
     *
     * @param request DTO contendo o ID do candidato.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/votos") // Endpoint mais RESTful para registrar votos
    public ResponseEntity<String> votar(@Valid @RequestBody VotoRequest request) {
        try {
            blockchainService.votar(request.getIdCandidato()).get();
            return ResponseEntity
                    .ok("Voto registrado com sucesso para o candidato ID " + request.getIdCandidato() + ".");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao votar: " + e.getMessage());
        }
    }

    /**
     * Obtém o total de candidatos registrados no sistema.
     *
     * @return ResponseEntity com o número total de candidatos.
     */
    @GetMapping("/candidatos/total")
    public ResponseEntity<BigInteger> obterTotalDeCandidatos() {
        try {
            // Agora o método retorna BigInteger diretamente
            BigInteger total = (BigInteger) blockchainService.obterTotalDeCandidatos().get();
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            // Em caso de erro, retorna 500 Internal Server Error e um valor padrão
            return ResponseEntity.status(500).body(BigInteger.ZERO);
        }
    }

    /**
     * Autoriza ou desautoriza um eleitor no sistema de votação.
     * Recebe o endereço do eleitor e o status de autorização via DTO.
     *
     * @param request DTO contendo o endereço do eleitor e o status de autorização.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/eleitores/autorizar") // Endpoint mais RESTful para gerenciar eleitores
    public ResponseEntity<String> autorizarEleitor(@Valid @RequestBody AutorizarEleitorRequest request) {
        try {
            blockchainService.autorizarEleitor(request.getEnderecoEleitor(), request.isAutorizado()).get();
            String status = request.isAutorizado() ? "autorizado" : "desautorizado";
            return ResponseEntity
                    .ok("Eleitor com endereço '" + request.getEnderecoEleitor() + "' " + status + " com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao autorizar eleitor: " + e.getMessage());
        }
    }

    /**
     * Verifica se um determinado eleitor já votou.
     *
     * @param endereco Endereço Ethereum do eleitor.
     * @return ResponseEntity com o DTO EleitorResponse indicando se o eleitor votou
     *         ou não.
     */
    @GetMapping("/eleitores/{endereco}/votou") // Endpoint mais RESTful para verificar status de eleitor
    public ResponseEntity<EleitorResponse> verificarSeVotou(@PathVariable String endereco) {
        try {
            Boolean votou = blockchainService.verificarSeVotou(endereco).get();
            return ResponseEntity.ok(new EleitorResponse(endereco, votou));
        } catch (Exception e) {
            // Em caso de erro, retorna 500 Internal Server Error e um DTO com status falso
            return ResponseEntity.status(500).body(new EleitorResponse(endereco, false));
        }
    }
}