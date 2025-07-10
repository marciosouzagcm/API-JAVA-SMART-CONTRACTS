package blockchain.api_java_smart_contracts.model.dto;

import java.math.BigInteger; // Importe BigInteger para nonce
import jakarta.validation.constraints.NotBlank; // Para Strings que não podem ser vazias ou nulas
import jakarta.validation.constraints.NotNull; // Para campos obrigatórios
import jakarta.validation.constraints.Pattern; // Para validar o formato do endereço Ethereum
import jakarta.validation.constraints.Positive; // Para números positivos

public class VotoRequest {

    // ID do candidato: Corresponde ao ID do seu banco de dados
    @NotNull(message = "O ID do candidato não pode ser nulo.")
    @Positive(message = "O ID do candidato deve ser um número positivo.")
    private Long idCandidato;

    // Endereço Ethereum do eleitor: Será validado on-chain se já votou
    @NotBlank(message = "O endereço do eleitor não pode ser vazio.")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Endereço de eleitor inválido. Deve ser um endereço Ethereum de 42 caracteres, começando com '0x'.")
    private String voterAddress;

    // Nonce: Um número único para a transação, para prevenir ataques de replay
    @NotNull(message = "O nonce não pode ser nulo.")
    @Positive(message = "O nonce deve ser um número positivo.")
    private BigInteger nonce;

    // Hash da mensagem assinada: Deve ser o hash gerado e assinado pelo trustedSigner (geralmente começa com "0x")
    @NotBlank(message = "O hash da mensagem assinada não pode ser vazio.")
    @Pattern(regexp = "^0x[a-fA-F0-9]{64}$", message = "Hash da mensagem assinada inválido. Deve ser um hash SHA256/Keccak256 de 66 caracteres, começando com '0x'.")
    private String signedMessageHash;

    // Assinatura: A assinatura digital gerada pelo trustedSigner (geralmente começa com "0x")
    @NotBlank(message = "A assinatura não pode ser vazia.")
    @Pattern(regexp = "^0x[a-fA-F0-9]+$", message = "Assinatura inválida. Deve ser uma string hexadecimal, começando com '0x'.")
    private String signature;

    // --- Construtores ---
    public VotoRequest() {
        // Construtor padrão necessário para a desserialização de JSON
    }

    public VotoRequest(Long idCandidato, String voterAddress, BigInteger nonce, String signedMessageHash, String signature) {
        this.idCandidato = idCandidato;
        this.voterAddress = voterAddress;
        this.nonce = nonce;
        this.signedMessageHash = signedMessageHash;
        this.signature = signature;
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

    public String getSignedMessageHash() {
        return signedMessageHash;
    }

    public void setSignedMessageHash(String signedMessageHash) {
        this.signedMessageHash = signedMessageHash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}