package blockchain.api_java_smart_contracts.model.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;

public class AutorizarEleitorRequest {

    @NotBlank(message = "O endereço do eleitor não pode ser vazio.")
    private String enderecoEleitor; // Endereço Ethereum do eleitor a ser autorizado/desautorizado

    private boolean autorizado; // true para autorizar, false para desautorizar

    public AutorizarEleitorRequest() {
        // Construtor padrão vazio para desserialização (ex: de JSON)
    }

    public AutorizarEleitorRequest(String enderecoEleitor, boolean autorizado) {
        this.enderecoEleitor = enderecoEleitor;
        this.autorizado = autorizado;
    }

    public String getEnderecoEleitor() {
        return enderecoEleitor;
    }

    public void setEnderecoEleitor(String enderecoEleitor) {
        this.enderecoEleitor = enderecoEleitor;
    }

    // Para booleanos, a convenção em Java é usar 'is' no getter
    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    @Override
    public String toString() {
        return "AutorizarEleitorRequest{" +
                "enderecoEleitor='" + enderecoEleitor + '\'' +
                ", autorizado=" + autorizado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AutorizarEleitorRequest that = (AutorizarEleitorRequest) o;
        return autorizado == that.autorizado && Objects.equals(enderecoEleitor, that.enderecoEleitor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enderecoEleitor, autorizado);
    }
}