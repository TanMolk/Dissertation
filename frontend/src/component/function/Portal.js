import AccountUtil from "../../utils/AccountUtil";
import {useEffect, useState} from "react";
import {Card, Col, Row, Statistic} from "antd";
import BlockchainConstant from "../../constants/BlockchainConstant";
import DocumentIntegrity from "../../constants/DocumentIntegrity.json"

const filterOptions = {
    fromBlock: 0,
    toBlock: 'latest'
};

export default function Portal() {

    const [blockNumber, setBlockNumber] = useState(undefined);
    const [gasPrice, setGasPrice] = useState(undefined);
    const [certificationAmount, setCertificationAmount] = useState([]);
    const [systemJoinTransactionAmount, setSystemJoinTransactionAmount] = useState(undefined);
    const [dataLoading, setDataLoading] = useState(true);


    const getBlockChainData = async () => {
        const contract = new AccountUtil.web3Client.eth.Contract(DocumentIntegrity.abi,
            BlockchainConstant.CONTRACT_ADDRESS);


        setBlockNumber(await AccountUtil.web3Client.eth.getBlockNumber());
        setGasPrice(await AccountUtil.web3Client.eth.getGasPrice());
        setCertificationAmount(await contract.getPastEvents(
            "AddCertificationEvent",
            filterOptions
        ));
        setSystemJoinTransactionAmount(await AccountUtil.web3Client.eth
            .getTransactionCount(BlockchainConstant.BACKEND_ADDRESS));
        setDataLoading(false);
    }

    useEffect(() => {
        getBlockChainData();
    }, [])

    return (
        <Row
            style={{marginLeft: "2rem"}}
            gutter={[16, 16]}
        >
            <Col span={12}>
                <Card>
                    <Statistic
                        title={"Current Block Height"}
                        value={blockNumber}
                    />
                </Card>
            </Col>
            <Col span={12}>
                <Card>
                    <Statistic
                        title={"Gas Price"}
                        value={gasPrice}
                    />
                </Card>
            </Col>
            <Col span={12}>
                <Card>
                    <Statistic
                        title={"Certification Number (on Chain)"}
                        value={certificationAmount.length}
                        loading={dataLoading}
                    />
                </Card>
            </Col>
            <Col span={12}>
                <Card>
                    <Statistic
                        title={"Tx Number this system joining"}
                        value={systemJoinTransactionAmount}
                        loading={dataLoading}
                    />
                </Card></Col>
        </Row>
    );
}