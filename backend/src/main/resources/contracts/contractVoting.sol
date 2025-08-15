// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

// Ajuste o import para o caminho relativo dentro do seu projeto
import "./openzeppelin/contracts/utils/cryptography/ECDSA.sol";

/// @title Contrato de Votação
/// @author marciosouzagcm@gmail.com
/// @dev Este contrato implementa um sistema de votação onde a autenticação de eleitores é feita off-chain via assinaturas digitais,
///      e a votação é registrada on-chain. Candidatos são gerenciados *externamente* ao contrato.
contract contractVoting {
    using ECDSA for bytes32; // Habilita métodos ECDSA para bytes32

    // Mapeamento: Mapeia o ID do candidato (vindo do DB) para sua contagem de votos
    mapping(uint256 => uint256) public candidateVotes;

    /// @notice Mapeia o endereço de um eleitor para verificar se ele já votou.
    mapping(address => bool) public hasVoted;

    /// @notice Endereço da conta confiável (sua API Spring Boot) que tem permissão para assinar votos válidos.
    address public trustedSigner;

    /// @notice Variável que armazena o número total de votos registrados no contrato.
    uint256 public totalVotesCount;

    /// @notice Evento emitido quando um voto é registrado com sucesso.
    event VotoRegistrado(uint256 idCandidato, address eleitor);

    /// @notice Evento emitido quando ocorre uma tentativa de voto inválida.
    event VotoFalhou(uint256 idCandidato, address eleitor, string motivo);

    modifier apenasTrustedSigner() {
        require(msg.sender == trustedSigner, "Apenas o assinante confiavel pode executar esta funcao.");
        _;
    }

    /// @notice Construtor do contrato. Define o endereço da conta que implantou o contrato como o trustedSigner inicial.
    constructor() {
        trustedSigner = msg.sender;
    }

    /// @notice Permite ao trustedSigner definir um novo trustedSigner.
    /// @param _newSigner O novo endereço que será o trustedSigner.
    function setTrustedSigner(address _newSigner) public apenasTrustedSigner {
        require(_newSigner != address(0), "Endereco do novo assinante confiavel invalido.");
        trustedSigner = _newSigner;
    }

    /// @notice Função para obter a contagem de votos de um candidato específico.
    /// @dev Este ID deve corresponder ao ID do candidato no seu banco de dados.
    /// @param _idCandidato O ID do candidato a ser consultado (vindo do DB).
    /// @return (uint256) Retorna a contagem de votos do candidato.
    function getCandidateVotes(uint256 _idCandidato) public view returns (uint256) {
        // Retorna 0 se o candidato não tiver votos, o que é o comportamento esperado para mappings.
        return candidateVotes[_idCandidato];
    }

    /// @notice Função para um eleitor votar em um candidato, com autenticação via assinatura.
    /// @param _candidateId O ID do candidato em quem o eleitor deseja votar (este ID vem do DB).
    /// @param _voterAddress O endereço Ethereum do eleitor que está votando (validado off-chain).
    /// @param _nonce Mantenha este parâmetro se seu processo de assinatura o usa para prevenir replay attacks.
    /// @param _signedMessageHash O hash da mensagem assinada (ex: keccak256(abi.encodePacked(...))).
    /// @param _signature A assinatura digital gerada pelo `trustedSigner`.
    function votar(
        uint256 _candidateId,
        address _voterAddress,
        uint256 _nonce, // Mantenha ou remova dependendo do uso real na assinatura
        bytes32 _signedMessageHash,
        bytes memory _signature
    ) public {
        // 1. Verificar se a assinatura é válida e vem do `trustedSigner`
        address signer = _signedMessageHash.recover(_signature);
        require(signer == trustedSigner, "Assinatura invalida ou nao do assinante confiavel.");

        // 2. Verificar se o eleitor já votou
        if (hasVoted[_voterAddress]) {
            emit VotoFalhou(_candidateId, _voterAddress, "Eleitor ja votou.");
            revert("Eleitor ja votou."); // Reverte a transação se o eleitor já votou
        }

        // 3. Registrar o voto
        hasVoted[_voterAddress] = true; // Marca o eleitor como tendo votado
        candidateVotes[_candidateId]++; // Incrementa a contagem de votos do candidato
        totalVotesCount++; // Incrementa a contagem total de votos

        emit VotoRegistrado(_candidateId, _voterAddress);
    }

    /// @notice Função para verificar se um determinado endereço já votou.
    /// @param _eleitor O endereço do eleitor a ser verificado.
    /// @return (bool) Retorna true se o eleitor já votou, false caso contrário.
    function verificarSeVotou(address _eleitor) public view returns (bool) {
        return hasVoted[_eleitor];
    }

    /// @notice Retorna o número total de votos registrados no contrato.
    function getTotalVotesCount() public view returns (uint256) {
        return totalVotesCount;
    }
}