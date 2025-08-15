package blockchain.api_java_smart_contracts.controller;

import blockchain.api_java_smart_contracts.model.Candidato;
import blockchain.api_java_smart_contracts.repository.CandidatoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Adicione esta importação
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciar operações relacionadas a Candidatos.
 * Expõe endpoints para registrar, listar, buscar e atualizar candidatos.
 */
@RestController
@RequestMapping("/api/candidatos")
@Tag(name = "Candidatos", description = "Gerenciamento de Candidatos para votação")
public class CandidatoController {

    @Autowired
    private CandidatoRepository candidatoRepository;

    /**
     * Registra um novo candidato no sistema.
     * @param candidato O objeto Candidato a ser registrado.
     * @return ResponseEntity com o candidato salvo e status 201 CREATED.
     */
    @Operation(summary = "Registrar um novo candidato",
                description = "Cria um novo registro de candidato no banco de dados.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do Candidato a ser registrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Candidato.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "201", description = "Candidato registrado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Candidato.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (dados ausentes ou mal formatados)"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @PostMapping
    public ResponseEntity<Candidato> registrarCandidato(@Valid @RequestBody Candidato candidato) {
        Candidato novoCandidato = candidatoRepository.save(candidato);
        return new ResponseEntity<>(novoCandidato, HttpStatus.CREATED);
    }

    /**
     * Lista todos os candidatos registrados.
     * @return ResponseEntity com uma lista de candidatos e status 200 OK.
     */
    @Operation(summary = "Listar todos os candidatos",
                description = "Retorna uma lista de todos os candidatos registrados no sistema.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de candidatos recuperada com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Candidato.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @GetMapping
    public ResponseEntity<List<Candidato>> listarCandidatos() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        return new ResponseEntity<>(candidatos, HttpStatus.OK);
    }

    /**
     * Busca um candidato pelo ID.
     * @param id O ID do candidato.
     * @return ResponseEntity com o candidato encontrado e status 200 OK, ou 404 NOT FOUND.
     */
    @Operation(summary = "Buscar candidato por ID",
                description = "Retorna um candidato específico pelo seu ID.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Candidato encontrado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Candidato.class))),
                    @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @GetMapping("/{id}")
    public ResponseEntity<Candidato> buscarCandidatoPorId(@PathVariable Long id) {
        Optional<Candidato> candidato = candidatoRepository.findById(id);
        return candidato.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Atualiza um candidato existente.
     * @param id O ID do candidato a ser atualizado.
     * @param candidatoAtualizado O objeto Candidato com os dados atualizados.
     * @return ResponseEntity com o candidato atualizado e status 200 OK, ou 404 NOT FOUND.
     */
    @Operation(summary = "Atualizar um candidato existente",
                description = "Atualiza os dados de um candidato existente pelo seu ID.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados do Candidato",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Candidato.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Candidato atualizado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Candidato.class))),
                    @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @PutMapping("/{id}")
    public ResponseEntity<Candidato> atualizarCandidato(@PathVariable Long id, @Valid @RequestBody Candidato candidatoAtualizado) {
        return candidatoRepository.findById(id)
                .map(candidato -> {
                    // Atualiza os campos nome e descricao
                    candidato.setNome(candidatoAtualizado.getNome());
                    candidato.setDescricao(candidatoAtualizado.getDescricao());
                    Candidato salvo = candidatoRepository.save(candidato);
                    return new ResponseEntity<>(salvo, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Exclui um candidato pelo ID.
     * @param id O ID do candidato a ser excluído.
     * @return ResponseEntity com status 204 NO CONTENT se a exclusão for bem-sucedida, ou 404 NOT FOUND.
     */
    @Operation(summary = "Excluir um candidato",
                description = "Remove um candidato do sistema pelo seu ID.",
                responses = {
                    // Para 204 No Content, o 'content' é opcional pois não há corpo de resposta esperado.
                    @ApiResponse(responseCode = "204", description = "Candidato excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCandidato(@PathVariable Long id) {
        if (candidatoRepository.existsById(id)) {
            candidatoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}