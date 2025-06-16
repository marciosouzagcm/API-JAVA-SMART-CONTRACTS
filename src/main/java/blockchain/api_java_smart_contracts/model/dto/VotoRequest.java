package blockchain.api_java_smart_contracts.model.dto;

import java.math.BigInteger;
import java.util.Objects;

public class VotoRequest {

    private BigInteger idCandidato; // O ID do candidato em que o voto será registrado

    public VotoRequest() {
        // Construtor padrão vazio para desserialização (ex: de JSON)
    }

    public VotoRequest(BigInteger idCandidato) {
        this.idCandidato = idCandidato;
    }

    public BigInteger getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(BigInteger idCandidato) {
        this.idCandidato = idCandidato;
    }

    @Override
    public String toString() {
        return "VotoRequest{" +
               "idCandidato=" + idCandidato +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotoRequest that = (VotoRequest) o;
        return Objects.equals(idCandidato, that.idCandidato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCandidato);
    }
}