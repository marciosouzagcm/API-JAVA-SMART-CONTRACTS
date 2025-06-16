package blockchain.api_java_smart_contracts.model.dto;

import java.util.Objects;

public class EleitorResponse {

    private String enderecoEleitor; // Endereço Ethereum do eleitor
    private boolean jaVotou; // Indica se o eleitor já exerceu seu voto

    public EleitorResponse() {
        // Construtor padrão vazio
    }

    public EleitorResponse(String enderecoEleitor, boolean jaVotou) {
        this.enderecoEleitor = enderecoEleitor;
        this.jaVotou = jaVotou;
    }

    public String getEnderecoEleitor() {
        return enderecoEleitor;
    }

    public void setEnderecoEleitor(String enderecoEleitor) {
        this.enderecoEleitor = enderecoEleitor;
    }

    public boolean isJaVotou() { // Para booleanos, a convenção é 'isNomeDaPropriedade'
        return jaVotou;
    }

    public void setJaVotou(boolean jaVotou) {
        this.jaVotou = jaVotou;
    }

    @Override
    public String toString() {
        return "EleitorResponse{" +
                "enderecoEleitor='" + enderecoEleitor + '\'' +
                ", jaVotou=" + jaVotou +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EleitorResponse that = (EleitorResponse) o;
        return jaVotou == that.jaVotou && Objects.equals(enderecoEleitor, that.enderecoEleitor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enderecoEleitor, jaVotou);
    }
}