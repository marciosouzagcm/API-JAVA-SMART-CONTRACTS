package blockchain.api_java_smart_contracts.model;

import java.math.BigInteger;
import java.util.Objects;

public class Candidato {

    private BigInteger id;
    private String nome;
    private BigInteger votos; // Mantendo o nome 'votos' para consistência interna

    public Candidato() {
        // Construtor padrão sem argumentos
    }

    public Candidato(BigInteger id, String nome, BigInteger votos) {
        this.id = id;
        this.nome = nome;
        this.votos = votos;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigInteger getVotos() {
        return votos;
    }

    public void setVotos(BigInteger votos) {
        this.votos = votos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidato candidato = (Candidato) o;
        // Comparar apenas id e nome geralmente é suficiente para igualdade lógica,
        // mas dependerá do seu caso de uso (se votos também define a identidade)
        return Objects.equals(id, candidato.id) && Objects.equals(nome, candidato.nome) && Objects.equals(votos, candidato.votos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, votos);
    }

    @Override
    public String toString() {
        return "Candidato{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", votos=" + votos +
               '}';
    }
}