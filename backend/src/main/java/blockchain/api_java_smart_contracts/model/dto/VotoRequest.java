package blockchain.api_java_smart_contracts.model.dto;

import java.math.BigInteger; // Importe BigInteger para nonce
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class VotoRequest {

    @NotNull(message = "O ID do candidato não pode ser nulo.")
    @Positive(message = "O ID do candidato deve ser um número positivo.")
    private Long idCandidato;

    @NotBlank(message = "O endereço do eleitor não pode ser vazio.")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Endereço de eleitor inválido. Deve ser um endereço Ethereum de 42 caracteres, começando com '0x'.")
    private String voterAddress;

    // O nonce é mantido aqui se o cliente FOR GERAR e ENVIAR o nonce.
    // Se a sua API Spring Boot SEMPRE for gerar o nonce, você pode considerar
    // torná-lo opcional ou até removê-lo daqui e gerá-lo inteiramente no serviço.
    // Por enquanto, vamos mantê-lo, assumindo que o cliente pode gerar ou sua API usará isso como base.
    @NotNull(message = "O nonce não pode ser nulo.")
    @Positive(message = "O nonce deve ser um número positivo.")
    private BigInteger nonce;

    // REMOVIDOS: signedMessageHash e signature não são mais enviados pelo cliente
    // private String signedMessageHash;
    // private String signature;

    // --- Construtores ---
    public VotoRequest() {
        // Construtor padrão necessário para a desserialização de JSON
    }

    public VotoRequest(Long idCandidato, String voterAddress, BigInteger nonce) {
        this.idCandidato = idCandidato;
        this.voterAddress = voterAddress;
        this.nonce = nonce;
    }

    // --- Getters e Setters ---
    public Long getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Long idCandidato) {
        this.idCandidato = idCandidato;
    }

    public String getVoterAddress() {
        return voterAddress;
    }

    public void setVoterAddress(String voterAddress) {
        this.voterAddress = voterAddress;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    // Os getters e setters para signedMessageHash e signature foram removidos.
}