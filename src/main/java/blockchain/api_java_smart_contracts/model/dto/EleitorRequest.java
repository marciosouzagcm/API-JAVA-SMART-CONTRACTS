package blockchain.api_java_smart_contracts.model.dto;

import java.util.Objects;

public class EleitorRequest {

    private String enderecoEleitor; // Endereço Ethereum do eleitor

    public EleitorRequest() {
        // Construtor padrão vazio
    }

    public EleitorRequest(String enderecoEleitor) {
        this.enderecoEleitor = enderecoEleitor;
    }

    public String getEnderecoEleitor() {
        return enderecoEleitor;
    }

    public void setEnderecoEleitor(String enderecoEleitor) {
        this.enderecoEleitor = enderecoEleitor;
    }

    @Override
    public String toString() {
        return "EleitorRequest{" +
               "enderecoEleitor='" + enderecoEleitor + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EleitorRequest that = (EleitorRequest) o;
        return Objects.equals(enderecoEleitor, that.enderecoEleitor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enderecoEleitor);
    }
}