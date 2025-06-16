package com.votacao.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.0.
 */
@SuppressWarnings("rawtypes")
public class Voting extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ADICIONARCANDIDATO = "adicionarCandidato";

    public static final String FUNC_ADMINISTRADOR = "administrador";

    public static final String FUNC_AUTORIZARELEITOR = "autorizarEleitor";

    public static final String FUNC_CANDIDATOS = "candidatos";

    public static final String FUNC_CONTADORDECANDIDATOS = "contadorDeCandidatos";

    public static final String FUNC_ELEITORES = "eleitores";

    public static final String FUNC_OBTERCANDIDATO = "obterCandidato";

    public static final String FUNC_OBTERTOTALDECANDIDATOS = "obterTotalDeCandidatos";

    public static final String FUNC_VERIFICARSEVOTOU = "verificarSeVotou";

    public static final String FUNC_VOTAR = "votar";

    public static final Event CANDIDATOADICIONADO_EVENT = new Event("CandidatoAdicionado", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ELEITORAUTORIZADO_EVENT = new Event("EleitorAutorizado", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event VOTOFALHOU_EVENT = new Event("VotoFalhou", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTOREGISTRADO_EVENT = new Event("VotoRegistrado", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CandidatoAdicionadoEventResponse> getCandidatoAdicionadoEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CANDIDATOADICIONADO_EVENT, transactionReceipt);
        ArrayList<CandidatoAdicionadoEventResponse> responses = new ArrayList<CandidatoAdicionadoEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CandidatoAdicionadoEventResponse typedResponse = new CandidatoAdicionadoEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.nome = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CandidatoAdicionadoEventResponse getCandidatoAdicionadoEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CANDIDATOADICIONADO_EVENT, log);
        CandidatoAdicionadoEventResponse typedResponse = new CandidatoAdicionadoEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.nome = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<CandidatoAdicionadoEventResponse> candidatoAdicionadoEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCandidatoAdicionadoEventFromLog(log));
    }

    public Flowable<CandidatoAdicionadoEventResponse> candidatoAdicionadoEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATOADICIONADO_EVENT));
        return candidatoAdicionadoEventFlowable(filter);
    }

    public static List<EleitorAutorizadoEventResponse> getEleitorAutorizadoEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ELEITORAUTORIZADO_EVENT, transactionReceipt);
        ArrayList<EleitorAutorizadoEventResponse> responses = new ArrayList<EleitorAutorizadoEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EleitorAutorizadoEventResponse typedResponse = new EleitorAutorizadoEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.autorizado = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EleitorAutorizadoEventResponse getEleitorAutorizadoEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ELEITORAUTORIZADO_EVENT, log);
        EleitorAutorizadoEventResponse typedResponse = new EleitorAutorizadoEventResponse();
        typedResponse.log = log;
        typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.autorizado = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<EleitorAutorizadoEventResponse> eleitorAutorizadoEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEleitorAutorizadoEventFromLog(log));
    }

    public Flowable<EleitorAutorizadoEventResponse> eleitorAutorizadoEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ELEITORAUTORIZADO_EVENT));
        return eleitorAutorizadoEventFlowable(filter);
    }

    public static List<VotoFalhouEventResponse> getVotoFalhouEvents(TransactionReceipt transactionReceipt) {
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

    public Flowable<VotoFalhouEventResponse> votoFalhouEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTOFALHOU_EVENT));
        return votoFalhouEventFlowable(filter);
    }

    public static List<VotoRegistradoEventResponse> getVotoRegistradoEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTOREGISTRADO_EVENT, transactionReceipt);
        ArrayList<VotoRegistradoEventResponse> responses = new ArrayList<VotoRegistradoEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotoRegistradoEventResponse typedResponse = new VotoRegistradoEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.nome = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotoRegistradoEventResponse getVotoRegistradoEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTOREGISTRADO_EVENT, log);
        VotoRegistradoEventResponse typedResponse = new VotoRegistradoEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.nome = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.eleitor = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<VotoRegistradoEventResponse> votoRegistradoEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotoRegistradoEventFromLog(log));
    }

    public Flowable<VotoRegistradoEventResponse> votoRegistradoEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTOREGISTRADO_EVENT));
        return votoRegistradoEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> adicionarCandidato(String _nome) {
        final Function function = new Function(
                FUNC_ADICIONARCANDIDATO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_nome)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> administrador() {
        final Function function = new Function(FUNC_ADMINISTRADOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> autorizarEleitor(String _eleitor, Boolean _autorizado) {
        final Function function = new Function(
                FUNC_AUTORIZARELEITOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _eleitor), 
                new org.web3j.abi.datatypes.Bool(_autorizado)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>> candidatos(BigInteger param0) {
        final Function function = new Function(FUNC_CANDIDATOS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> contadorDeCandidatos() {
        final Function function = new Function(FUNC_CONTADORDECANDIDATOS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<Boolean, Boolean, BigInteger>> eleitores(String param0) {
        final Function function = new Function(FUNC_ELEITORES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<Boolean, Boolean, BigInteger>>(function,
                new Callable<Tuple3<Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple3<Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<Boolean, Boolean, BigInteger>(
                                (Boolean) results.get(0).getValue(), 
                                (Boolean) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>> obterCandidato(BigInteger _id) {
        final Function function = new Function(FUNC_OBTERCANDIDATO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, String, BigInteger>>(function,
                new Callable<Tuple3<BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> obterTotalDeCandidatos() {
        final Function function = new Function(FUNC_OBTERTOTALDECANDIDATOS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> verificarSeVotou(String _eleitor) {
        final Function function = new Function(FUNC_VERIFICARSEVOTOU, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _eleitor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> votar(BigInteger _id) {
        final Function function = new Function(
                FUNC_VOTAR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class CandidatoAdicionadoEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String nome;
    }

    public static class EleitorAutorizadoEventResponse extends BaseEventResponse {
        public String eleitor;

        public Boolean autorizado;
    }

    public static class VotoFalhouEventResponse extends BaseEventResponse {
        public BigInteger idCandidato;

        public String eleitor;

        public String motivo;
    }

    public static class VotoRegistradoEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String nome;

        public String eleitor;
    }
}
