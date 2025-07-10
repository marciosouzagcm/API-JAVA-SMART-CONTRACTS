package blockchain.api_java_smart_contracts.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.ContractGasProvider;

import com.votacao.api.exception.EleitorException;
import com.votacao.contracts.ContractVoting; // Classe gerada pelo Web3j

@Service
// Removendo UnnecessaryReturnStatement porque pode não ser aplicável após as remoções
@SuppressWarnings("unused") // Mantendo "unused" para métodos de DB que podem não ser chamados diretamente na demo
public class VotingService {

    private final Web3j web3j;
    private final String contractAddress;
    private final ContractGasProvider gasProvider;
    private final Credentials adminCredentials;
    private final ContractVoting votingContract;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate; // Removido 'final' para permitir @Autowired

    private static final Logger LOGGER = Logger.getLogger(VotingService.class.getName());

    @Autowired
    public VotingService(
            @Qualifier("web3jConnection") Web3j web3jConnection,
            @Value("${blockchain.voting.contract-address}") String contractAddressValue,
            @Qualifier("credentialsContract") Credentials credentialsContract,
            ContractGasProvider injectedGasProvider,
            @Qualifier("votingContract") ContractVoting adminVotingContract) {

        this.web3j = web3jConnection;
        this.contractAddress = contractAddressValue;
        this.adminCredentials = credentialsContract;
        this.gasProvider = injectedGasProvider;
        this.votingContract = adminVotingContract;
    }

    // --- MÉTODOS AJUSTADOS/REMOVIDOS BASEADOS NO CONTRACTVOTING.SOL ---

    // REMOVIDO: O contrato Solidity não tem mais 'adicionarCandidato'.
    // A gestão de candidatos (nomes, IDs) deve ser feita no seu banco de dados.
    /*
    public String adicionarCandidato(String nome) throws Exception {
        // Implementação original removida pois a funcionalidade não está no contrato Solidity
        // Você precisará gerenciar candidatos no seu DB e apenas usar o ID para votar.
        return "Funcionalidade de adicionar candidato movida para gerenciamento off-chain.";
    }
    */

    // REMOVIDO: O contrato Solidity não tem mais 'autorizarEleitor'.
    // A autorização é feita implicitamente pela assinatura do trustedSigner no método 'votar'.
    /*
    public String autorizarEleitor(String eleitorAddress) throws Exception {
        // Implementação original removida pois a funcionalidade não está no contrato Solidity
        return "Funcionalidade de autorizar eleitor movida para autenticação por assinatura.";
    }
    */

    /**
     * Obtém a lista de todos os candidatos e suas contagens de votos do contrato.
     * ATENÇÃO: O contrato NÃO ARMAZENA os nomes dos candidatos.
     * Este método agora busca os votos para IDs de candidatos, supondo que os IDs
     * são gerenciados externamente (ex: em um banco de dados).
     * Retorna uma lista de pares (ID do Candidato, Contagem de Votos).
     * Se você precisa dos nomes, eles devem vir do seu banco de dados.
     */
    public List<VotoCandidatoDTO> obterVotosDosCandidatosExistentes(List<BigInteger> candidateIdsFromDB) throws Exception {
        LOGGER.log(Level.INFO, "Iniciando a busca por votos de candidatos na blockchain.");
        List<VotoCandidatoDTO> resultados = new ArrayList<>();
        try {
            for (BigInteger idCandidato : candidateIdsFromDB) {
                // CHAMADA DO MÉTODO CORRIGIDA: De candidates para getCandidateVotes
                BigInteger votos = votingContract.getCandidateVotes(idCandidato).send();
                resultados.add(new VotoCandidatoDTO(idCandidato, votos));
                LOGGER.log(Level.INFO, "ID Candidato {0}: {1} votos", new Object[] { idCandidato, votos });
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter votos dos candidatos: {0}", e.getMessage());
            throw e;
        }
        return resultados;
    }

    // DTO auxiliar para retornar os votos, já que o nome do candidato não está no contrato
    public static class VotoCandidatoDTO {
        private BigInteger idCandidato;
        private BigInteger votos;

        public VotoCandidatoDTO(BigInteger idCandidato, BigInteger votos) {
            this.idCandidato = idCandidato;
            this.votos = votos;
        }

        public BigInteger getIdCandidato() { return idCandidato; }
        public BigInteger getVotos() { return votos; }
    }


    /**
     * Chama a função `votar` do smart contract `ContractVoting`.
     * A assinatura do método corresponde à do contrato Solidity.
     *
     * @param idCandidato O ID do candidato em quem votar.
     * @param eleitorAddress O endereço Ethereum do eleitor.
     * @param nonce O nonce da transação, usado para evitar ataques de replay.
     * @param signedMessageHash O hash da mensagem assinada pelo trustedSigner.
     * @param signature A assinatura digital do trustedSigner.
     */
    public String votar(BigInteger idCandidato, String eleitorAddress, BigInteger nonce, byte[] signedMessageHash, byte[] signature) throws Exception {
        LOGGER.log(Level.INFO, "Registrando voto para o candidato de índice: {0} pelo eleitor: {1}",
                new Object[]{idCandidato, eleitorAddress});
        // CHAMADA DO MÉTODO CORRIGIDA: Nome do método `votar` no contrato.
        TransactionReceipt receipt = votingContract.votar(idCandidato, eleitorAddress, nonce, signedMessageHash, signature).send();
        LOGGER.log(Level.INFO, "Voto para {0} registrado. Hash da Transação: {1}",
                new Object[] { idCandidato, receipt.getTransactionHash() });
        return receipt.getTransactionHash();
    }

    /**
     * Sobrecarga do método `votar` para quando as credenciais do eleitor
     * são passadas como chave privada, e a transação é enviada usando estas credenciais.
     *
     * @param idCandidato O ID do candidato em quem votar.
     * @param eleitorPrivateKey A chave privada do eleitor para assinar a transação.
     * @param eleitorAddress O endereço Ethereum do eleitor.
     * @param nonce O nonce da transação, usado para evitar ataques de replay.
     * @param signedMessageHash O hash da mensagem assinada pelo trustedSigner.
     * @param signature A assinatura digital do trustedSigner.
     */
    public String votar(BigInteger idCandidato, String eleitorPrivateKey, String eleitorAddress, BigInteger nonce, byte[] signedMessageHash, byte[] signature) throws Exception {
        LOGGER.log(Level.INFO, "Registrando voto para o candidato de índice {0} com credenciais específicas do eleitor: {1}.",
                new Object[]{idCandidato, eleitorAddress});
        Credentials eleitorCredentials = Credentials.create(eleitorPrivateKey);
        // CARREGANDO CONTRATO COM AS CREDENCIAIS DO ELEITOR
        ContractVoting eleitorVotingContract = ContractVoting.load(contractAddress, web3j, eleitorCredentials, gasProvider);

        // CHAMADA DO MÉTODO CORRIGIDA: Nome do método `votar` no contrato.
        TransactionReceipt receipt = eleitorVotingContract.votar(idCandidato, eleitorAddress, nonce, signedMessageHash, signature).send();
        LOGGER.log(Level.INFO, "Voto para {0} registrado. Hash da Transação: {1}",
                new Object[] { idCandidato, receipt.getTransactionHash() });
        return receipt.getTransactionHash();
    }

    /**
     * Obtém a contagem de votos de um candidato específico diretamente da blockchain.
     * Nome do método ajustado para `getCandidateVotes`.
     */
    public BigInteger obterVotos(BigInteger candidateIndex) throws Exception {
        LOGGER.log(Level.INFO, "Buscando votos para o candidato de índice: {0}", candidateIndex);
        try {
            // CHAMADA DO MÉTODO CORRIGIDA: De candidates para getCandidateVotes
            BigInteger votes = votingContract.getCandidateVotes(candidateIndex).send();
            LOGGER.log(Level.INFO, "Votos obtidos para o candidato {0}: {1}", new Object[] { candidateIndex, votes });
            return votes;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter votos para o candidato de índice '{0}': {1}",
                    new Object[] { candidateIndex, e.getMessage() });
            throw e;
        }
    }

    /**
     * Método para autorizar um eleitor, verificando suas credenciais em um banco de dados.
     * ATENÇÃO: A lógica de autorização no contrato foi alterada para assinaturas.
     * Este método pode ser adaptado para *verificar* credenciais no DB e depois
     * preparar os dados para o método `votar` com assinatura.
     * Por enquanto, lança exceção, pois o contrato não tem um método `autorizarEleitor` direto.
     */
    public String autorizarEleitorComCredenciais(String endereco, String usuario, String senha)
            throws EleitorException {
        if (jdbcTemplate == null) {
            LOGGER.log(Level.WARNING, "JdbcTemplate não está configurado. Não é possível verificar credenciais no DB.");
            throw new EleitorException("Serviço de verificação de credenciais não disponível.");
        }
        if (verificarCredenciais(usuario, senha)) {
            LOGGER.log(Level.INFO, "Eleitor {0} autenticado via DB. Endereço: {1}", new Object[]{usuario, endereco});
            // O contrato não tem um método 'autorizarEleitor' separado.
            // A autorização é implícita na chamada do 'votar' com a assinatura do trustedSigner.
            // Se esta função for usada, ela deve apenas autenticar o eleitor no DB
            // e depois o processo de votação deve gerar e usar a assinatura.
            // Por simplicidade, vou lançar uma exceção indicando que a funcionalidade mudou.
            throw new EleitorException("A autorização de eleitor é agora baseada em assinatura no contrato, não um método direto.");
        } else {
            throw new EleitorException("Credenciais inválidas.");
        }
    }

    /**
     * Verifica as credenciais de um eleitor em um banco de dados usando JdbcTemplate.
     */
    private boolean verificarCredenciais(String usuario, String senha) {
        String sql = "SELECT COUNT(*) FROM autorizarEleitor WHERE login = ? AND senha = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuario, senha);
        return count != null && count > 0;
    }

    /**
     * Verifica o status da conexão com o nó Ethereum.
     */
    public String verificarStatusContrato() {
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            return "Conectado ao nó Ethereum: " + clientVersion.getWeb3ClientVersion();
        } catch (Exception e) {
            return "Erro ao conectar ao nó Ethereum: " + e.getMessage();
        }
    }

    /**
     * Método para definir um novo Trusted Signer no contrato.
     * Este método deve ser chamado apenas pelo proprietário do contrato.
     * @param newSignerAddress O novo endereço do trusted signer.
     * @return Hash da transação.
     * @throws Exception se a transação falhar.
     */
    public String setTrustedSigner(String newSignerAddress) throws Exception {
        LOGGER.log(Level.INFO, "Tentando definir novo Trusted Signer: {0}", newSignerAddress);
        TransactionReceipt receipt = votingContract.setTrustedSigner(newSignerAddress).send();
        LOGGER.log(Level.INFO, "Novo Trusted Signer definido. Hash da Transação: {0}", receipt.getTransactionHash());
        return receipt.getTransactionHash();
    }

    /**
     * Verifica se um eleitor já votou no contrato.
     * @param eleitorAddress O endereço do eleitor.
     * @return true se já votou, false caso contrário.
     * @throws Exception se a chamada falhar.
     */
    public boolean verificarSeEleitorVotou(String eleitorAddress) throws Exception {
        LOGGER.log(Level.INFO, "Verificando se o eleitor {0} já votou.", eleitorAddress);
        // CHAMADA DO MÉTODO CORRIGIDA: Nome do método `verificarSeVotou` no contrato.
        return votingContract.verificarSeVotou(eleitorAddress).send();
    }
}