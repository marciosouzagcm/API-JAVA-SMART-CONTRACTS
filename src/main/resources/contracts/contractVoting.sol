// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

// Ajuste o import para o caminho relativo dentro do seu projeto
import "./openzeppelin/contracts/utils/cryptography/ECDSA.sol"; // <--- CAMINHO AJUSTADO

/// @title Contrato de Votação
/// @author marciosouzagcm@gmail.com
/// @dev Este contrato implementa um sistema de votação onde a autenticação de eleitores é feita off-chain via assinaturas digitais,
///      e a votação é registrada on-chain. Candidatos são gerenciados *externamente* ao contrato.
contract contractVoting {
    using ECDSA for bytes32; // Habilita métodos ECDSA para bytes32

    // Novo mapeamento: Mapeia o ID do candidato (vindo do DB) para sua contagem de votos
    mapping(uint256 => uint256) public candidateVotes;

    /// @notice Mapeia o endereço de um eleitor para verificar se ele já votou.
    mapping(address => bool) public hasVoted;

    /// @notice Endereço da conta confiável (sua API Spring Boot) que tem permissão para assinar votos válidos.
    address public trustedSigner;

    /// @notice Evento emitido quando um voto é registrado com sucesso.
    // O nome do candidato agora virá da sua aplicação Java/DB
    event VotoRegistrado(uint256 idCandidato, address eleitor);

    /// @notice Evento emitido quando ocorre uma tentativa de voto inválida.
    event VotoFalhou(uint256 idCandidato, address eleitor, string motivo);

    // Modifier 'apenasProprietario' agora é 'apenasTrustedSigner'
    modifier apenasTrustedSigner() {
        require(msg.sender == trustedSigner, "Apenas o assinante confiavel pode executar esta funcao.");
        _;
    }

    /// @notice Construtor do contrato. Define o endereço da conta que implantou o contrato como o trustedSigner inicial.
    constructor() {
        trustedSigner = msg.sender;
    }

    /// @notice Permite ao proprietário do contrato definir um novo trustedSigner.
    /// @param _newSigner O novo endereço que será o trustedSigner.
    function setTrustedSigner(address _newSigner) public apenasTrustedSigner {
        require(_newSigner != address(0), "Endereco do novo assinante confiavel invalido.");
        trustedSigner = _newSigner;
    }

    // --- FUNÇÕES REMOVIDAS/AJUSTADAS ---
    // Estrutura Candidato - REMOVIDA
    // mapping(uint256 => Candidato) public candidatos; - REMOVIDA
    // uint256 public contadorDeCandidatos; - REMOVIDA (O total de candidatos é gerenciado no DB)
    // event CandidatoAdicionado(uint256 id, string nome); - REMOVIDA
    // function adicionarCandidatoContrato(string memory _nome) public apenasProprietario - REMOVIDA

    /// @notice Função para obter a contagem de votos de um candidato específico.
    /// @dev Este ID deve corresponder ao ID do candidato no seu banco de dados.
    /// @param _idCandidato O ID do candidato a ser consultado (vindo do DB).
    /// @return (uint256) Retorna a contagem de votos do candidato.
    function getCandidateVotes(uint256 _idCandidato) public view returns (uint256) {
        // Não precisamos de 'require' aqui se o contrato apenas armazena votos para IDs recebidos.
        // Se um ID nunca recebeu votos, retornará 0.
        return candidateVotes[_idCandidato];
    }

    /// @notice Função para um eleitor votar em um candidato, com autenticação via assinatura.
    /// @param _candidateId O ID do candidato em quem o eleitor deseja votar (este ID vem do DB).
    /// @param _voterAddress O endereço Ethereum do eleitor que está votando (validado off-chain).
    /// @param _signedMessageHash O hash da mensagem assinada (ex: keccak256(abi.encodePacked(...))).
    /// @param _signature A assinatura digital gerada pelo `trustedSigner`.
    function votar(
        uint256 _candidateId,
        address _voterAddress,
        uint256 /* _nonce */, // Mantenha _nonce se seu processo de assinatura o usa
        bytes32 _signedMessageHash,
        bytes memory _signature
    ) public {
        // 1. Verificar se a assinatura é válida e vem do `trustedSigner`
        address signer = _signedMessageHash.recover(_signature);
        require(signer == trustedSigner, "Assinatura invalida ou nao do assinante confiavel.");

        // 2. Verificar se o eleitor já votou
        if (hasVoted[_voterAddress]) {
            emit VotoFalhou(_candidateId, _voterAddress, "Eleitor ja votou.");
            revert("Eleitor ja votou.");
        }

        // 3. Registrar o voto
        hasVoted[_voterAddress] = true; // Marca o eleitor como tendo votado
        candidateVotes[_candidateId]++; // Incrementa a contagem de votos do candidato

        // No evento, não temos mais o nome do candidato, apenas o ID
        emit VotoRegistrado(_candidateId, _voterAddress);
    }

    /// @notice Função para verificar se um determinado endereço já votou.
    /// @param _eleitor O endereço do eleitor a ser verificado.
    /// @return (bool) Retorna true se o eleitor já votou, false caso contrário.
    function verificarSeVotou(address _eleitor) public view returns (bool) {
        return hasVoted[_eleitor];
    }

    /// @notice Esta função não é mais necessária, pois o número de candidatos é gerenciado no DB.
    /// @return (uint256) Retorna 0, pois o contrato não mantém essa contagem.
    function obterTotalDeCandidatos() public pure returns (uint256) {
        return 0; // O contrato não sabe o total de candidatos do DB
    }
}