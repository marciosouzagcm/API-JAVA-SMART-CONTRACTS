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
    public static final String BINARY = "6080604052348015600e575f5ffd5b50600280546001600160a01b031916331790556108388061002e5f395ff3fe608060405234801561000f575f5ffd5b5060043610610085575f3560e01c80636a29e438116100585780636a29e438146100f8578063866163c014610123578063f49e323e14610142578063f74d548014610161575f5ffd5b806309eef43e146100895780631bd50f7e146100c05780634bc8b0c6146100d557806356a1c701146100e5575b5f5ffd5b6100ab61009736600461066a565b60016020525f908152604090205460ff1681565b60405190151581526020015b60405180910390f35b6100d36100ce36600461069e565b61018c565b005b5f5b6040519081526020016100b7565b6100d36100f336600461066a565b61034b565b6100ab61010636600461066a565b6001600160a01b03165f9081526001602052604090205460ff1690565b6100d761013136600461077d565b5f9081526020819052604090205490565b6100d761015036600461077d565b5f6020819052908152604090205481565b600254610174906001600160a01b031681565b6040516001600160a01b0390911681526020016100b7565b5f610197838361045a565b6002549091506001600160a01b038083169116146102175760405162461bcd60e51b815260206004820152603260248201527f417373696e617475726120696e76616c696461206f75206e616f20646f20617360448201527139b4b730b73a329031b7b73334b0bb32b61760711b60648201526084015b60405180910390fd5b6001600160a01b0385165f9081526001602052604090205460ff16156102c3577f01a4a1d83d9b244a1b2e53c56209aa17af46aea7d79d34b0908d2d926bc6fe3086866040516102a39291909182526001600160a01b031660208201526060604082018190526011908201527022b632b4ba37b9103530903b37ba37ba9760791b608082015260a00190565b60405180910390a160405162461bcd60e51b815260040161020e90610794565b6001600160a01b0385165f908152600160208181526040808420805460ff191690931790925588835282905281208054916102fd836107ca565b9091555050604080518781526001600160a01b03871660208201527f88836221e51a97e955a94feece1d0abdd999fbb2cec6f33d4bfdf3bc5250a3d5910160405180910390a1505050505050565b6002546001600160a01b031633146103cb5760405162461bcd60e51b815260206004820152603760248201527f4170656e6173206f20617373696e616e746520636f6e66696176656c20706f6460448201527f6520657865637574617220657374612066756e63616f2e000000000000000000606482015260840161020e565b6001600160a01b0381166104385760405162461bcd60e51b815260206004820152602e60248201527f456e64657265636f20646f206e6f766f20617373696e616e746520636f6e666960448201526d30bb32b61034b73b30b634b2379760911b606482015260840161020e565b600280546001600160a01b0319166001600160a01b0392909216919091179055565b5f5f5f5f6104688686610482565b92509250925061047882826104cb565b5090949350505050565b5f5f5f83516041036104b9576020840151604085015160608601515f1a6104ab88828585610587565b9550955095505050506104c4565b505081515f91506002905b9250925092565b5f8260038111156104de576104de6107ee565b036104e7575050565b60018260038111156104fb576104fb6107ee565b036105195760405163f645eedf60e01b815260040160405180910390fd5b600282600381111561052d5761052d6107ee565b0361054e5760405163fce698f760e01b81526004810182905260240161020e565b6003826003811115610562576105626107ee565b03610583576040516335e2f38360e21b81526004810182905260240161020e565b5050565b5f80807f7fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a08411156105c057505f91506003905082610645565b604080515f808252602082018084528a905260ff891692820192909252606081018790526080810186905260019060a0016020604051602081039080840390855afa158015610611573d5f5f3e3d5ffd5b5050604051601f1901519150506001600160a01b03811661063c57505f925060019150829050610645565b92505f91508190505b9450945094915050565b80356001600160a01b0381168114610665575f5ffd5b919050565b5f6020828403121561067a575f5ffd5b6106838261064f565b9392505050565b634e487b7160e01b5f52604160045260245ffd5b5f5f5f5f5f60a086880312156106b2575f5ffd5b853594506106c26020870161064f565b93506040860135925060608601359150608086013567ffffffffffffffff8111156106eb575f5ffd5b8601601f810188136106fb575f5ffd5b803567ffffffffffffffff8111156107155761071561068a565b604051601f8201601f19908116603f0116810167ffffffffffffffff811182821017156107445761074461068a565b6040528181528282016020018a101561075b575f5ffd5b816020840160208301375f602083830101528093505050509295509295909350565b5f6020828403121561078d575f5ffd5b5035919050565b602081525f6107c460208301601181527022b632b4ba37b9103530903b37ba37ba9760791b602082015260400190565b92915050565b5f600182016107e757634e487b7160e01b5f52601160045260245ffd5b5060010190565b634e487b7160e01b5f52602160045260245ffdfea264697066735822122025c4ce6188ee1428112bcbbd428b472963049976e364dc0a093ec1a1dc2eae3b64736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CANDIDATEVOTES = "candidateVotes";

    public static final String FUNC_GETCANDIDATEVOTES = "getCandidateVotes";

    public static final String FUNC_HASVOTED = "hasVoted";

    public static final String FUNC_OBTERTOTALDECANDIDATOS = "obterTotalDeCandidatos";

    public static final String FUNC_SETTRUSTEDSIGNER = "setTrustedSigner";

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

    public RemoteFunctionCall<Boolean> hasVoted(String param0) {
        final Function function = new Function(FUNC_HASVOTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> obterTotalDeCandidatos() {
        final Function function = new Function(FUNC_OBTERTOTALDECANDIDATOS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setTrustedSigner(String _newSigner) {
        final Function function = new Function(
                FUNC_SETTRUSTEDSIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _newSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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
