package blockchain.api_java_smart_contracts.model.dto;

import java.util.Objects; // Importar java.util.Objects para os métodos equals e hashCode

public class AdicionarCandidatoRequest { // Nome da classe em PascalCase

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Construtor vazio (necessário para desserialização do JSON, por exemplo, pelo Spring/Jackson)
    public AdicionarCandidatoRequest() {
    }

    // Construtor com todos os argumentos
    public AdicionarCandidatoRequest(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "AdicionarCandidatoRequest{" +
               "nome='" + nome + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdicionarCandidatoRequest that = (AdicionarCandidatoRequest) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}