package blockchain.api_java_smart_contracts.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdicionarCandidatoRequest {

    @NotBlank(message = "O nome do candidato não pode ser vazio")
    @Size(max = 100, message = "O nome do candidato não pode exceder 100 caracteres")
    private String nome;

    @Size(max = 500, message = "A descrição do candidato não pode exceder 500 caracteres")
    private String descricao; // Adicionado para persistência no DB

    public AdicionarCandidatoRequest() {}

    public AdicionarCandidatoRequest(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
