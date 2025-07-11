package org.web3j.model;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.CustomError;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.14.0.
 */
@SuppressWarnings("rawtypes")
public class ContractVoting extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b50600280546001600160a01b031916331790556108668061002e5f395ff3fe608060405234801561000f575f5ffd5b5060043610610090575f3560e01c8063866163c011610063578063866163c01461011e578063bf734b861461014b578063f396d67414610153578063f49e323e1461015c578063f74d54801461017b575f5ffd5b806309eef43e146100945780631bd50f7e146100cb57806356a1c701146100e05780636a29e438146100f3575b5f5ffd5b6100b66100a2366004610698565b60016020525f908152604090205460ff1681565b60405190151581526020015b60405180910390f35b6100de6100d93660046106cc565b6101a6565b005b6100de6100ee366004610698565b610379565b6100b6610101366004610698565b6001600160a01b03165f9081526001602052604090205460ff1690565b61013d61012c3660046107ab565b5f9081526020819052604090205490565b6040519081526020016100c2565b60035461013d565b61013d60035481565b61013d61016a3660046107ab565b5f6020819052908152604090205481565b60025461018e906001600160a01b031681565b6040516001600160a01b0390911681526020016100c2565b5f6101b18383610488565b6002549091506001600160a01b038083169116146102315760405162461bcd60e51b815260206004820152603260248201527f417373696e617475726120696e76616c696461206f75206e616f20646f20617360448201527139b4b730b73a329031b7b73334b0bb32b61760711b60648201526084015b60405180910390fd5b6001600160a01b0385165f9081526001602052604090205460ff16156102dd577f01a4a1d83d9b244a1b2e53c56209aa17af46aea7d79d34b0908d2d926bc6fe3086866040516102bd9291909182526001600160a01b031660208201526060604082018190526011908201527022b632b4ba37b9103530903b37ba37ba9760791b608082015260a00190565b60405180910390a160405162461bcd60e51b8152600401610228906107c2565b6001600160a01b0385165f908152600160208181526040808420805460ff19169093179092558883528290528120805491610317836107f8565b909155505060038054905f61032b836107f8565b9091555050604080518781526001600160a01b03871660208201527f88836221e51a97e955a94feece1d0abdd999fbb2cec6f33d4bfdf3bc5250a3d5910160405180910390a1505050505050565b6002546001600160a01b031633146103f95760405162461bcd60e51b815260206004820152603760248201527f4170656e6173206f20617373696e616e746520636f6e66696176656c20706f6460448201527f6520657865637574617220657374612066756e63616f2e0000000000000000006064820152608401610228565b6001600160a01b0381166104665760405162461bcd60e51b815260206004820152602e60248201527f456e64657265636f20646f206e6f766f20617373696e616e746520636f6e666960448201526d30bb32b61034b73b30b634b2379760911b6064820152608401610228565b600280546001600160a01b0319166001600160a01b0392909216919091179055565b5f5f5f5f61049686866104b0565b9250925092506104a682826104f9565b5090949350505050565b5f5f5f83516041036104e7576020840151604085015160608601515f1a6104d9888285856105b5565b9550955095505050506104f2565b505081515f91506002905b9250925092565b5f82600381111561050c5761050c61081c565b03610515575050565b60018260038111156105295761052961081c565b036105475760405163f645eedf60e01b815260040160405180910390fd5b600282600381111561055b5761055b61081c565b0361057c5760405163fce698f760e01b815260048101829052602401610228565b60038260038111156105905761059061081c565b036105b1576040516335e2f38360e21b815260048101829052602401610228565b5050565b5f80807f7fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a08411156105ee57505f91506003905082610673565b604080515f808252602082018084528a905260ff891692820192909252606081018790526080810186905260019060a0016020604051602081039080840390855afa15801561063f573d5f5f3e3d5ffd5b5050604051601f1901519150506001600160a01b03811661066a57505f925060019150829050610673565b92505f91508190505b9450945094915050565b80356001600160a01b0381168114610693575f5ffd5b919050565b5f602082840312156106a8575f5ffd5b6106b18261067d565b9392505050565b634e487b7160e01b5f52604160045260245ffd5b5f5f5f5f5f60a086880312156106e0575f5ffd5b853594506106f06020870161067d565b93506040860135925060608601359150608086013567ffffffffffffffff811115610719575f5ffd5b8601601f81018813610729575f5ffd5b803567ffffffffffffffff811115610743576107436106b8565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610772576107726106b8565b6040528181528282016020018a1015610789575f5ffd5b816020840160208301375f602083830101528093505050509295509295909350565b5f602082840312156107bb575f5ffd5b5035919050565b602081525f6107f260208301601181527022b632b4ba37b9103530903b37ba37ba9760791b602082015260400190565b92915050565b5f6001820161081557634e487b7160e01b5f52601160045260245ffd5b5060010190565b634e487b7160e01b5f52602160045260245ffdfea2646970667358221220e2c77f2d25014338deb8741d06dd3c9a65fd9c7831d10ce87b3abd381955a98e64736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CANDIDATEVOTES = "candidateVotes";

    public static final String FUNC_GETCANDIDATEVOTES = "getCandidateVotes";

    public static final String FUNC_GETTOTALVOTESCOUNT = "getTotalVotesCount";

    public static final String FUNC_HASVOTED = "hasVoted";

    public static final String FUNC_SETTRUSTEDSIGNER = "setTrustedSigner";

    public static final String FUNC_TOTALVOTESCOUNT = "totalVotesCount";

    public static final String FUNC_TRUSTEDSIGNER = "trustedSigner";

    public static final String FUNC_VERIFICARSEVOTOU = "verificarSeVotou";

    public static final String FUNC_VOTAR = "votar";

    public static final CustomError ECDSAINVALIDSIGNATURE_ERROR = new CustomError("ECDSAInvalidSignature", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final CustomError ECDSAINVALIDSIGNATURELENGTH_ERROR = new CustomError("ECDSAInvalidSignatureLength", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final CustomError ECDSAINVALIDSIGNATURES_ERROR = new CustomError("ECDSAInvalidSignatureS", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
    ;

    public static final Event VOTOFALHOU_EVENT = new Event("VotoFalhou", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTOREGISTRADO_EVENT = new Event("VotoRegistrado", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected ContractVoting(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ContractVoting(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ContractVoting(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ContractVoting(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<VotoFalhouEventResponse> getVotoFalhouEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTOFALHOU_EVENT, transactionReceipt);
        ArrayList<VotoFalhouEventResponse> responses = new ArrayList<VotoFalhouEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotoFalhouEventResponse typedResponse = new VotoFalhouEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.idCandidato = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.motivo = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotoFalhouEventResponse getVotoFalhouEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTOFALHOU_EVENT, log);
        VotoFalhouEventResponse typedResponse = new VotoFalhouEventResponse();
        typedResponse.log = log;
        typedResponse.idCandidato = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.motivo = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<VotoFalhouEventResponse> votoFalhouEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotoFalhouEventFromLog(log));
    }

    public Flowable<VotoFalhouEventResponse> votoFalhouEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTOFALHOU_EVENT));
        return votoFalhouEventFlowable(filter);
    }

    public static List<VotoRegistradoEventResponse> getVotoRegistradoEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTOREGISTRADO_EVENT, transactionReceipt);
        ArrayList<VotoRegistradoEventResponse> responses = new ArrayList<VotoRegistradoEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotoRegistradoEventResponse typedResponse = new VotoRegistradoEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.idCandidato = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotoRegistradoEventResponse getVotoRegistradoEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTOREGISTRADO_EVENT, log);
        VotoRegistradoEventResponse typedResponse = new VotoRegistradoEventResponse();
        typedResponse.log = log;
        typedResponse.idCandidato = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<VotoRegistradoEventResponse> votoRegistradoEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotoRegistradoEventFromLog(log));
    }

    public Flowable<VotoRegistradoEventResponse> votoRegistradoEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTOREGISTRADO_EVENT));
        return votoRegistradoEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> candidateVotes(BigInteger param0) {
        final Function function = new Function(FUNC_CANDIDATEVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getCandidateVotes(BigInteger _idCandidato) {
        final Function function = new Function(FUNC_GETCANDIDATEVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_idCandidato)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalVotesCount() {
        final Function function = new Function(FUNC_GETTOTALVOTESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> hasVoted(String param0) {
        final Function function = new Function(FUNC_HASVOTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setTrustedSigner(String _newSigner) {
        final Function function = new Function(
                FUNC_SETTRUSTEDSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _newSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalVotesCount() {
        final Function function = new Function(FUNC_TOTALVOTESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> trustedSigner() {
        final Function function = new Function(FUNC_TRUSTEDSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> verificarSeVotou(String _eleitor) {
        final Function function = new Function(FUNC_VERIFICARSEVOTOU, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _eleitor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> votar(BigInteger _candidateId,
            String _voterAddress, BigInteger param2, byte[] _signedMessageHash, byte[] _signature) {
        final Function function = new Function(
                FUNC_VOTAR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_candidateId), 
                new org.web3j.abi.datatypes.Address(160, _voterAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.web3j.abi.datatypes.generated.Bytes32(_signedMessageHash), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ContractVoting load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new ContractVoting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ContractVoting load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ContractVoting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ContractVoting load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new ContractVoting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ContractVoting load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ContractVoting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ContractVoting> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ContractVoting.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<ContractVoting> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ContractVoting.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ContractVoting> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ContractVoting.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ContractVoting> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ContractVoting.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class VotoFalhouEventResponse extends BaseEventResponse {
        public BigInteger idCandidato;

        public String eleitor;

        public String motivo;
    }

    public static class VotoRegistradoEventResponse extends BaseEventResponse {
        public BigInteger idCandidato;

        public String eleitor;
    }
}
