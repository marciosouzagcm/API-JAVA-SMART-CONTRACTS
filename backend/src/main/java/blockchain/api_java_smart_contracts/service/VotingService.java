package blockchain.api_java_smart_contracts.service;

// Importação necessária para manipular bytes:
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import com.votacao.api.exception.EleitorException;
import com.votacao.contracts.ContractVoting;

@Service
@SuppressWarnings("unused")
public class VotingService {

    private final Web3j web3j;
    private final String contractAddress;
    private final ContractGasProvider gasProvider;
    private final Credentials adminCredentials;
    private final ContractVoting votingContract;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

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

    public List<VotoCandidatoDTO> obterVotosDosCandidatosExistentes(List<BigInteger> candidateIdsFromDB)
            throws Exception {
        LOGGER.log(Level.INFO, "Iniciando a busca por votos de candidatos na blockchain.");
        List<VotoCandidatoDTO> resultados = new ArrayList<>();
        try {
            for (BigInteger idCandidato : candidateIdsFromDB) {
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

    public static class VotoCandidatoDTO {
        private BigInteger idCandidato;
        private BigInteger votos;

        public VotoCandidatoDTO(BigInteger idCandidato, BigInteger votos) {
            this.idCandidato = idCandidato;
            this.votos = votos;
        }

        public BigInteger getIdCandidato() {
            return idCandidato;
        }

        public BigInteger getVotos() {
            return votos;
        }
    }

    public String registerVoteWithSignature(BigInteger candidateId, String voterAddress, BigInteger nonce)
            throws Exception {
        LOGGER.log(Level.INFO,
                "Preparando para registrar voto para o candidato de ID: {0} pelo eleitor: {1} com nonce: {2}",
                new Object[] { candidateId, voterAddress, nonce });

        // --- INÍCIO DA NOVA ABORDAGEM PARA encodePacked ---
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Encode e concatene uint256 candidateId
            // Uint256 são 32 bytes. Certifique-se de que o valor esteja formatado
            // corretamente para 32 bytes.
            outputStream.write(Numeric.toBytesPadded(candidateId, 32));

            // Encode e concatene address voterAddress
            // Address são 20 bytes.
            outputStream.write(Numeric.hexStringToByteArray(voterAddress));

            // Encode e concatene uint256 nonce
            // Uint256 são 32 bytes.
            outputStream.write(Numeric.toBytesPadded(nonce, 32));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao concatenar bytes para encodePacked: {0}", e.getMessage());
            throw new RuntimeException("Erro ao processar dados para assinatura.", e);
        }

        byte[] encodedPacked = outputStream.toByteArray();
        // --- FIM DA NOVA ABORDAGEM PARA encodePacked ---

        byte[] messageHashBytes = Hash.sha3(encodedPacked);
        String signedMessageHashHex = Numeric.toHexString(messageHashBytes);
        LOGGER.log(Level.INFO, "Hash da mensagem a ser assinada: {0}", signedMessageHashHex);

        Sign.SignatureData signatureData = Sign.signMessage(
                messageHashBytes,
                adminCredentials.getEcKeyPair(),
                false);

        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        byte v = signatureData.getV()[0];

        byte[] combinedSignature = new byte[65];
        System.arraycopy(r, 0, combinedSignature, 0, 32);
        System.arraycopy(s, 0, combinedSignature, 32, 32);
        combinedSignature[64] = v;
        String signatureHex = Numeric.toHexString(combinedSignature);
        LOGGER.log(Level.INFO, "Assinatura gerada: {0}", signatureHex);

        TransactionReceipt receipt = votingContract.votar(
                candidateId,
                voterAddress,
                nonce,
                messageHashBytes,
                combinedSignature).send();

        LOGGER.log(Level.INFO, "Voto registrado na blockchain. Hash da Transação: {0}", receipt.getTransactionHash());
        return receipt.getTransactionHash();
    }

    @Deprecated
    public String votar(BigInteger idCandidato, String eleitorPrivateKey, String eleitorAddress, BigInteger nonce,
            byte[] signedMessageHash, byte[] signature) throws Exception {
        LOGGER.log(Level.INFO,
                "Registrando voto para o candidato de índice {0} com credenciais específicas do eleitor: {1}.",
                new Object[] { idCandidato, eleitorAddress });
        Credentials eleitorCredentials = Credentials.create(eleitorPrivateKey);
        ContractVoting eleitorVotingContract = ContractVoting.load(contractAddress, web3j, eleitorCredentials,
                gasProvider);

        TransactionReceipt receipt = eleitorVotingContract
                .votar(idCandidato, eleitorAddress, nonce, signedMessageHash, signature).send();
        LOGGER.log(Level.INFO, "Voto para {0} registrado. Hash da Transação: {1}",
                new Object[] { idCandidato, receipt.getTransactionHash() });
        return receipt.getTransactionHash();
    }

    public BigInteger obterVotos(BigInteger candidateIndex) throws Exception {
        LOGGER.log(Level.INFO, "Buscando votos para o candidato de índice: {0}", candidateIndex);
        try {
            BigInteger votes = votingContract.getCandidateVotes(candidateIndex).send();
            LOGGER.log(Level.INFO, "Votos obtidos para o candidato {0}: {1}", new Object[] { candidateIndex, votes });
            return votes;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao obter votos para o candidato de índice '{0}': {1}",
                    new Object[] { candidateIndex, e.getMessage() });
            throw e;
        }
    }

    public String autorizarEleitorComCredenciais(String endereco, String usuario, String senha)
            throws EleitorException {
        if (jdbcTemplate == null) {
            LOGGER.log(Level.WARNING, "JdbcTemplate não está configurado. Não é possível verificar credenciais no DB.");
            throw new EleitorException("Serviço de verificação de credenciais não disponível.");
        }
        if (verificarCredenciais(usuario, senha)) {
            LOGGER.log(Level.INFO, "Eleitor {0} autenticado via DB. Endereço: {1}", new Object[] { usuario, endereco });
            throw new EleitorException(
                    "A autorização de eleitor é agora baseada em assinatura no contrato, não um método direto.");
        } else {
            throw new EleitorException("Credenciais inválidas.");
        }
    }

    private boolean verificarCredenciais(String usuario, String senha) {
        String sql = "SELECT COUNT(*) FROM autorizarEleitor WHERE login = ? AND senha = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuario, senha);
        return count != null && count > 0;
    }

    public String verificarStatusContrato() {
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            return "Conectado ao nó Ethereum: " + clientVersion.getWeb3ClientVersion();
        } catch (Exception e) {
            return "Erro ao conectar ao nó Ethereum: " + e.getMessage();
        }
    }

    public String setTrustedSigner(String newSignerAddress) throws Exception {
        LOGGER.log(Level.INFO, "Tentando definir novo Trusted Signer: {0}", newSignerAddress);
        TransactionReceipt receipt = votingContract.setTrustedSigner(newSignerAddress).send();
        LOGGER.log(Level.INFO, "Novo Trusted Signer definido. Hash da Transação: {0}", receipt.getTransactionHash());
        return receipt.getTransactionHash();
    }

    public boolean verificarSeEleitorVotou(String eleitorAddress) throws Exception {
        LOGGER.log(Level.INFO, "Verificando se o eleitor {0} já votou.", eleitorAddress);
        return votingContract.verificarSeVotou(eleitorAddress).send();
    }
}