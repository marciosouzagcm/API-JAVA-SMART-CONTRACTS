package blockchain.api_java_smart_contracts.controller;

import blockchain.api_java_smart_contracts.model.Eleitor;
import blockchain.api_java_smart_contracts.repository.EleitorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Adicione esta importação para usar MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciar operações relacionadas a Eleitores.
 * Expõe endpoints para registrar, listar, buscar e atualizar eleitores.
 */
@RestController
@RequestMapping("/api/eleitores")
@Tag(name = "Eleitores", description = "Gerenciamento de Eleitores para votação")
public class EleitorController {

    @Autowired
    private EleitorRepository eleitorRepository;

    /**
     * Registra um novo eleitor no sistema.
     * @param eleitor O objeto Eleitor a ser registrado.
     * @return ResponseEntity com o eleitor salvo e status 201 CREATED.
     */
    @Operation(summary = "Registrar um novo eleitor",
                description = "Cria um novo registro de eleitor no banco de dados.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do Eleitor a ser registrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Eleitor.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "201", description = "Eleitor registrado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Eleitor.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (dados ausentes ou mal formatados)"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @PostMapping
    public ResponseEntity<Eleitor> registrarEleitor(@Valid @RequestBody Eleitor eleitor) {
        Eleitor novoEleitor = eleitorRepository.save(eleitor);
        return new ResponseEntity<>(novoEleitor, HttpStatus.CREATED);
    }

    /**
     * Lista todos os eleitores registrados.
     * @return ResponseEntity com uma lista de eleitores e status 200 OK.
     */
    @Operation(summary = "Listar todos os eleitores",
                description = "Retorna uma lista de todos os eleitores registrados no sistema.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de eleitores recuperada com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Eleitor.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @GetMapping
    public ResponseEntity<List<Eleitor>> listarEleitores() {
        List<Eleitor> eleitores = eleitorRepository.findAll();
        return new ResponseEntity<>(eleitores, HttpStatus.OK);
    }

    /**
     * Busca um eleitor pelo ID.
     * @param id O ID do eleitor.
     * @return ResponseEntity com o eleitor encontrado e status 200 OK, ou 404 NOT FOUND.
     */
    @Operation(summary = "Buscar eleitor por ID",
                description = "Retorna um eleitor específico pelo seu ID.",
                responses = {
                    @ApiResponse(responseCode = "200", description = "Eleitor encontrado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Eleitor.class))),
                    @ApiResponse(responseCode = "404", description = "Eleitor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @GetMapping("/{id}")
    public ResponseEntity<Eleitor> buscarEleitorPorId(@PathVariable Long id) {
        Optional<Eleitor> eleitor = eleitorRepository.findById(id);
        return eleitor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Atualiza um eleitor existente.
     * @param id O ID do eleitor a ser atualizado.
     * @param eleitorAtualizado O objeto Eleitor com os dados atualizados.
     * @return ResponseEntity com o eleitor atualizado e status 200 OK, ou 404 NOT FOUND.
     */
    @Operation(summary = "Atualizar um eleitor existente",
                description = "Atualiza os dados de um eleitor existente pelo seu ID.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados do Eleitor",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Eleitor.class))
                ),
                responses = {
                    @ApiResponse(responseCode = "200", description = "Eleitor atualizado com sucesso",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, // CORRIGIDO AQUI
                                                    schema = @Schema(implementation = Eleitor.class))),
                    @ApiResponse(responseCode = "404", description = "Eleitor não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @PutMapping("/{id}")
    public ResponseEntity<Eleitor> atualizarEleitor(@PathVariable Long id, @Valid @RequestBody Eleitor eleitorAtualizado) {
        return eleitorRepository.findById(id)
                .map(eleitor -> {
                    eleitor.setNome(eleitorAtualizado.getNome());
                    eleitor.setCpf(eleitorAtualizado.getCpf());
                    Eleitor salvo = eleitorRepository.save(eleitor);
                    return new ResponseEntity<>(salvo, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Exclui um eleitor pelo ID.
     * @param id O ID do eleitor a ser excluído.
     * @return ResponseEntity com status 204 NO CONTENT se a exclusão for bem-sucedida, ou 404 NOT FOUND.
     */
    @Operation(summary = "Excluir um eleitor",
                description = "Remove um eleitor do sistema pelo seu ID.",
                responses = {
                    // Para o caso de 204 No Content, geralmente não há corpo de resposta, então 'content' é opcional.
                    // Se você *quiser* um content, usaria content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    @ApiResponse(responseCode = "204", description = "Eleitor excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Eleitor não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
                })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEleitor(@PathVariable Long id) {
        if (eleitorRepository.existsById(id)) {
            eleitorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}