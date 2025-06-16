package blockchain.api_java_smart_contracts.model;

import java.util.Objects;

public class Eleitor {

    private String endereco; // Endereço Ethereum do eleitor
    private boolean jaVotou; // Indica se o eleitor já exerceu seu voto

    // Se o contrato tiver uma forma de "autorizar" eleitores e você quiser expor isso
    // private boolean autorizado;

    public Eleitor() {
        // Construtor padrão
    }

    public Eleitor(String endereco, boolean jaVotou) {
        this.endereco = endereco;
        this.jaVotou = jaVotou;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean getJaVotou() { // O nome do getter para boolean geralmente é isJaVotou()
        return jaVotou;
    }

    public void setJaVotou(boolean jaVotou) {
        this.jaVotou = jaVotou;
    }

    /*
    // Exemplo de getter/setter para 'autorizado' se necessário
    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }
    */

    @Override
    public String toString() {
        return "Eleitor{" +
               "endereco='" + endereco + '\'' +
               ", jaVotou=" + jaVotou +
               // (autorizado ? ", autorizado=" + autorizado : "") + // Adicionar se o campo for usado
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eleitor eleitor = (Eleitor) o;
        return jaVotou == eleitor.jaVotou && Objects.equals(endereco, eleitor.endereco);
        // && (autorizado == eleitor.autorizado) // Adicionar se o campo for usado
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco, jaVotou);
        // , autorizado // Adicionar se o campo for usado
    }
}