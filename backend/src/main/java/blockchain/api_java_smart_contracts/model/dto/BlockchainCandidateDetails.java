package blockchain.api_java_smart_contracts.model.dto;

import java.math.BigInteger;

// Este DTO representa os detalhes de um candidato conforme retornado diretamente da blockchain.
public class BlockchainCandidateDetails {
    private BigInteger blockchainId;
    private String name;
    private BigInteger votes;

    public BlockchainCandidateDetails(BigInteger blockchainId, String name, BigInteger votes) {
        this.blockchainId = blockchainId;
        this.name = name;
        this.votes = votes;
    }

    // Getters
    public BigInteger getBlockchainId() {
        return blockchainId;
    }

    public String getName() {
        return name;
    }

    public BigInteger getVotes() {
        return votes;
    }

    // Setters (opcional, dependendo de como você usa o DTO)
    public void setBlockchainId(BigInteger blockchainId) {
        this.blockchainId = blockchainId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVotes(BigInteger votes) {
        this.votes = votes;
    }
}