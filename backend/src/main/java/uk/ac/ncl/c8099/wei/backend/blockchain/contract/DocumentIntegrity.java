package uk.ac.ncl.c8099.wei.backend.blockchain.contract;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint48;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

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
public class DocumentIntegrity extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b50610a7f8061001d5f395ff3fe608060405234801561000f575f80fd5b506004361061004a575f3560e01c806362aa65f21461004e57806362daeb5d14610063578063a64dba7e1461008c578063ebd2caa9146100ac575b5f80fd5b61006161005c366004610595565b6100bf565b005b610076610071366004610672565b61015f565b60405161008391906106ef565b60405180910390f35b61009f61009a36600461074f565b6102b4565b604051610083919061076f565b6100616100ba366004610781565b61035e565b65ffffffffffff82165f90815260208190526040902080546100e09061084f565b1590506101085760405162461bcd60e51b81526004016100ff90610887565b60405180910390fd5b65ffffffffffff82165f9081526020819052604090206101288282610918565b5060405165ffffffffffff8316907fe0bcc6fe33dea3bf8d264fe46ebd27aff2929e6dcf00ca367a64e7c67f043d6d905f90a25050565b60605f825167ffffffffffffffff81111561017c5761017c6104e4565b6040519080825280602002602001820160405280156101af57816020015b606081526020019060019003908161019a5790505b5090505f5b83518110156102ad575f808583815181106101d1576101d16109d4565b602002602001015165ffffffffffff1665ffffffffffff1681526020019081526020015f2080546102019061084f565b80601f016020809104026020016040519081016040528092919081815260200182805461022d9061084f565b80156102785780601f1061024f57610100808354040283529160200191610278565b820191905f5260205f20905b81548152906001019060200180831161025b57829003601f168201915b505050505082828151811061028f5761028f6109d4565b602002602001018190525080806102a5906109e8565b9150506101b4565b5092915050565b65ffffffffffff81165f908152602081905260408120805460609291906102da9061084f565b80601f01602080910402602001604051908101604052809291908181526020018280546103069061084f565b80156103515780601f1061032857610100808354040283529160200191610351565b820191905f5260205f20905b81548152906001019060200180831161033457829003601f168201915b5093979650505050505050565b80518251146103a85760405162461bcd60e51b8152602060048201526016602482015275082e4e4c2f240d8cadccee8d0e640dad2e6dac2e8c6d60531b60448201526064016100ff565b5f5b8251811015610488575f808483815181106103c7576103c76109d4565b602002602001015165ffffffffffff1665ffffffffffff1681526020019081526020015f2080546103f79061084f565b1590506104165760405162461bcd60e51b81526004016100ff90610887565b818181518110610428576104286109d4565b60200260200101515f80858481518110610444576104446109d4565b602002602001015165ffffffffffff1665ffffffffffff1681526020019081526020015f2090816104759190610918565b5080610480816109e8565b9150506103aa565b50816040516104979190610a0c565b604051908190038120907fe17cb3bb983e1b9598a0d6147c9adf18353c0772ec0205dea514f6c3666f2b11905f90a25050565b803565ffffffffffff811681146104df575f80fd5b919050565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f1916810167ffffffffffffffff81118282101715610521576105216104e4565b604052919050565b5f82601f830112610538575f80fd5b813567ffffffffffffffff811115610552576105526104e4565b610565601f8201601f19166020016104f8565b818152846020838601011115610579575f80fd5b816020850160208301375f918101602001919091529392505050565b5f80604083850312156105a6575f80fd5b6105af836104ca565b9150602083013567ffffffffffffffff8111156105ca575f80fd5b6105d685828601610529565b9150509250929050565b5f67ffffffffffffffff8211156105f9576105f96104e4565b5060051b60200190565b5f82601f830112610612575f80fd5b81356020610627610622836105e0565b6104f8565b82815260059290921b84018101918181019086841115610645575f80fd5b8286015b848110156106675761065a816104ca565b8352918301918301610649565b509695505050505050565b5f60208284031215610682575f80fd5b813567ffffffffffffffff811115610698575f80fd5b6106a484828501610603565b949350505050565b5f81518084525f5b818110156106d0576020818501810151868301820152016106b4565b505f602082860101526020601f19601f83011685010191505092915050565b5f602080830181845280855180835260408601915060408160051b87010192508387015f5b8281101561074257603f198886030184526107308583516106ac565b94509285019290850190600101610714565b5092979650505050505050565b5f6020828403121561075f575f80fd5b610768826104ca565b9392505050565b602081525f61076860208301846106ac565b5f8060408385031215610792575f80fd5b823567ffffffffffffffff808211156107a9575f80fd5b6107b586838701610603565b93506020915081850135818111156107cb575f80fd5b8501601f810187136107db575f80fd5b80356107e9610622826105e0565b81815260059190911b82018401908481019089831115610807575f80fd5b8584015b8381101561083e57803586811115610822575f8081fd5b6108308c8983890101610529565b84525091860191860161080b565b508096505050505050509250929050565b600181811c9082168061086357607f821691505b60208210810361088157634e487b7160e01b5f52602260045260245ffd5b50919050565b60208082526023908201527f416c726561647920636f6e7461696e732074686973206365727469666963617460408201526234b7b760e91b606082015260800190565b601f821115610913575f81815260208120601f850160051c810160208610156108f05750805b601f850160051c820191505b8181101561090f578281556001016108fc565b5050505b505050565b815167ffffffffffffffff811115610932576109326104e4565b61094681610940845461084f565b846108ca565b602080601f831160018114610979575f84156109625750858301515b5f19600386901b1c1916600185901b17855561090f565b5f85815260208120601f198616915b828110156109a757888601518255948401946001909101908401610988565b50858210156109c457878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b5f52603260045260245ffd5b5f60018201610a0557634e487b7160e01b5f52601160045260245ffd5b5060010190565b81515f9082906020808601845b83811015610a3d57815165ffffffffffff1685529382019390820190600101610a19565b5092969550505050505056fea2646970667358221220837d3800f6f39a69d8104992b68e2b5130e463e41d5a7c90024b5083db9b293864736f6c63430008140033";

    public static final String FUNC_ADDCERTIFICATION = "addCertification";

    public static final String FUNC_ADDCERTIFICATIONS = "addCertifications";

    public static final String FUNC_GETCERTIFICATION = "getCertification";

    public static final String FUNC_GETCERTIFICATIONS = "getCertifications";

    public static final Event ADDCERTIFICATIONEVENT_EVENT = new Event("AddCertificationEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint48>(true) {}));
    ;

    public static final Event ADDCERTIFICATIONSEVENT_EVENT = new Event("AddCertificationsEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint48>>(true) {}));
    ;

    @Deprecated
    protected DocumentIntegrity(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DocumentIntegrity(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DocumentIntegrity(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DocumentIntegrity(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AddCertificationEventEventResponse> getAddCertificationEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADDCERTIFICATIONEVENT_EVENT, transactionReceipt);
        ArrayList<AddCertificationEventEventResponse> responses = new ArrayList<AddCertificationEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddCertificationEventEventResponse typedResponse = new AddCertificationEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AddCertificationEventEventResponse getAddCertificationEventEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ADDCERTIFICATIONEVENT_EVENT, log);
        AddCertificationEventEventResponse typedResponse = new AddCertificationEventEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<AddCertificationEventEventResponse> addCertificationEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAddCertificationEventEventFromLog(log));
    }

    public Flowable<AddCertificationEventEventResponse> addCertificationEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDCERTIFICATIONEVENT_EVENT));
        return addCertificationEventEventFlowable(filter);
    }

    public static List<AddCertificationsEventEventResponse> getAddCertificationsEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADDCERTIFICATIONSEVENT_EVENT, transactionReceipt);
        ArrayList<AddCertificationsEventEventResponse> responses = new ArrayList<AddCertificationsEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddCertificationsEventEventResponse typedResponse = new AddCertificationsEventEventResponse();
            typedResponse.log = eventValues.getLog();
//            typedResponse.ids = (byte[]) ((Array) eventValues.getIndexedValues().get(0)).getNativeValueCopy();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AddCertificationsEventEventResponse getAddCertificationsEventEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ADDCERTIFICATIONSEVENT_EVENT, log);
        AddCertificationsEventEventResponse typedResponse = new AddCertificationsEventEventResponse();
        typedResponse.log = log;
//        typedResponse.ids = (byte[]) ((Array) eventValues.getIndexedValues().get(0)).getNativeValueCopy();
        return typedResponse;
    }

    public Flowable<AddCertificationsEventEventResponse> addCertificationsEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAddCertificationsEventEventFromLog(log));
    }

    public Flowable<AddCertificationsEventEventResponse> addCertificationsEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDCERTIFICATIONSEVENT_EVENT));
        return addCertificationsEventEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addCertification(BigInteger id, String creatorSignature) {
        final Function function = new Function(
                FUNC_ADDCERTIFICATION, 
                Arrays.<Type>asList(new Uint48(id),
                new Utf8String(creatorSignature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addCertifications(List<BigInteger> idArr, List<String> creatorSignatures) {
        final Function function = new Function(
                FUNC_ADDCERTIFICATIONS, 
                Arrays.<Type>asList(new DynamicArray<Uint48>(
                        Uint48.class,
                        org.web3j.abi.Utils.typeMap(idArr, Uint48.class)),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(creatorSignatures, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getCertification(BigInteger id) {
        final Function function = new Function(FUNC_GETCERTIFICATION, 
                Arrays.<Type>asList(new Uint48(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getCertifications(List<BigInteger> idArr) {
        final Function function = new Function(FUNC_GETCERTIFICATIONS, 
                Arrays.<Type>asList(new DynamicArray<Uint48>(
                        Uint48.class,
                        org.web3j.abi.Utils.typeMap(idArr, Uint48.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static DocumentIntegrity load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentIntegrity(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DocumentIntegrity load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentIntegrity(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DocumentIntegrity load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DocumentIntegrity(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DocumentIntegrity load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DocumentIntegrity(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DocumentIntegrity> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentIntegrity.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DocumentIntegrity> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentIntegrity.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DocumentIntegrity> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentIntegrity.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DocumentIntegrity> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentIntegrity.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class AddCertificationEventEventResponse extends BaseEventResponse {
        public BigInteger id;
    }

    public static class AddCertificationsEventEventResponse extends BaseEventResponse {
        public byte[] ids;
    }
}
