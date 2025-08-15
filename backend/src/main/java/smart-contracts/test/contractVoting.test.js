// Importa o artefato do seu contrato
const contractVoting = artifacts.require("contractVoting");

// Importa a biblioteca web3 para trabalhar com endereços e hashes
const { web3 } = require("@openzeppelin/test-helpers/src/setup");

// Inicia o bloco de testes para o contrato 'contractVoting'
contract("contractVoting", accounts => {
  // Define os endereços a serem usados nos testes
  // accounts[0] será o deployer, que também é o trustedSigner
  // accounts[1] e accounts[2] serão os eleitores
  const [deployer, voter1, voter2] = accounts;

  // Variáveis para armazenar a instância do contrato e outros dados
  let votingInstance;
  const candidateId = 123; // ID de exemplo do seu banco de dados
  const message = "Votar no candidato 123";

  // Antes de cada teste, uma nova instância do contrato é implantada
  beforeEach(async () => {
    votingInstance = await contractVoting.new();
  });

  // --- Testes ---

  it("deve permitir que um eleitor vote se a assinatura for valida", async () => {
    // 1. Crie o hash da mensagem no formato certo
    const messageHash = web3.utils.soliditySha3(
      { type: "uint256", value: candidateId },
      { type: "address", value: voter1 }
      // Nota: Removi o 'nonce' e o 'signedMessageHash' para simplificar.
      // A sua função 'votar' precisaria de um 'messageHash' gerado.
      // O contrato parece esperar um hash específico que não é gerado com os 3 parâmetros da sua função 'votar'.
      // Para este teste, vamos assumir que a mensagem é 'candidateId' e 'voterAddress'.
    );

    // 2. Assine o hash da mensagem com a chave privada do trustedSigner (deployer)
    const { signature } = await web3.eth.accounts.sign(messageHash, web3.eth.accounts.privateKeyToAccount(web3.utils.keccak256(web3.eth.accounts.privateKeyToAccount(deployer).privateKey)).privateKey);
    // Nota: Essa linha de assinatura pode ser complexa. Depende de como a sua API assina a mensagem.
    // A maneira mais fácil de testar seria usar uma chave privada conhecida.

    // 3. Chame a função 'votar' do contrato
    await votingInstance.votar(candidateId, voter1, 0, messageHash, signature);

    // 4. Verifique se o voto foi registrado
    const totalVotes = await votingInstance.getTotalVotesCount();
    const candidateVotes = await votingInstance.getCandidateVotes(candidateId);
    const hasVoted = await votingInstance.hasVoted(voter1);

    // 5. Use asserções para verificar os resultados
    assert.equal(totalVotes.toNumber(), 1, "A contagem total de votos deve ser 1");
    assert.equal(candidateVotes.toNumber(), 1, "A contagem de votos do candidato deve ser 1");
    assert.equal(hasVoted, true, "O eleitor deve ter votado");
  });

  it("não deve permitir que o mesmo eleitor vote duas vezes", async () => {
    // Primeiro voto, com uma assinatura válida
    const messageHash1 = web3.utils.soliditySha3(
      { type: "uint256", value: candidateId },
      { type: "address", value: voter1 }
    );
    const { signature: signature1 } = await web3.eth.accounts.sign(messageHash1, web3.eth.accounts.privateKeyToAccount(web3.utils.keccak256(web3.eth.accounts.privateKeyToAccount(deployer).privateKey)).privateKey);
    await votingInstance.votar(candidateId, voter1, 0, messageHash1, signature1);

    // Tente votar uma segunda vez e espere que a transação falhe (reverta)
    try {
      await votingInstance.votar(candidateId, voter1, 0, messageHash1, signature1);
      // Se a linha acima não reverter, o teste falha
      assert.fail("O voto duplicado não deveria ser permitido");
    } catch (error) {
      assert.include(error.message, "Eleitor ja votou.", "O erro esperado não foi encontrado");
    }
  });

  it("não deve permitir um voto com uma assinatura inválida", async () => {
    // Crie uma assinatura com uma conta diferente (não o trustedSigner)
    const messageHash = web3.utils.soliditySha3(
      { type: "uint256", value: candidateId },
      { type: "address", value: voter2 }
    );
    const { signature } = await web3.eth.accounts.sign(messageHash, web3.eth.accounts.privateKeyToAccount(web3.utils.keccak256(web3.eth.accounts.privateKeyToAccount(voter2).privateKey)).privateKey);

    // Tente votar e espere que a transação falhe
    try {
      await votingInstance.votar(candidateId, voter2, 0, messageHash, signature);
      assert.fail("O voto com assinatura inválida não deveria ser permitido");
    } catch (error) {
      assert.include(error.message, "Assinatura invalida ou nao do assinante confiavel.", "O erro esperado não foi encontrado");
    }
  });
});