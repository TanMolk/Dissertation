import {Badge, Button, Descriptions, List, Modal, Popconfirm, Popover, Space, Steps} from "antd";
import {useEffect, useState} from "react";
import MessageUtil from "../../utils/MessageUtil";
import VerificationService from "../../service/VerificationService";
import InfiniteScroll from "react-infinite-scroll-component";
import VerificationUtil from "../../utils/VerificationUtil";
import {CheckOutlined, CloseOutlined, SearchOutlined} from "@ant-design/icons";
import RelatedPartyOperation from "./RelatedPartyOperation";
import ModalUtil from "../../utils/ModalUtil";
import CheckWithId from "./CheckWithId";
import AccountUtil from "../../utils/AccountUtil";


export default function VerificationInfo({id}) {
    const [verification, setVerification] = useState({});
    const [modalKey, setModalKey] = useState(0);
    const [modalShowState, setModalShowState] = useState(false);
    const [modalContent, setModalContent] = useState(null);


    useEffect(() => {
        async function updateInfo() {
            try {
                let info = await VerificationService.get(id);
                setVerification(info);
            } catch (e) {
                console.error(e);
                MessageUtil.error("Acquire verification fails!");
            }
        }

        updateInfo();
    }, [id]);

    const popContent =
        <div
            id="verification-info-process-list"
            style={{
                overflow: 'auto',
                padding: '0 16px',
                height:'40vh'
            }}
        >
            <InfiniteScroll
                dataLength={verification?.relatedParties?.length}
                next={() => {
                }}
                hasMore={false}
                loader={null}
                endMessage={null}
                scrollableTarget="verification-info-process-list"
            >
                <List
                    style={{width: "25vw"}}
                    itemLayout="horizontal"
                    dataSource={verification?.relatedParties}
                    renderItem={(item) => (
                        <List.Item>
                            <List.Item.Meta
                                title={AccountUtil.getRemarkDisplay(item.partyAddress,true)}
                                description={item.status !== "PENDING" ? item.status : "WAITING"}
                            />
                        </List.Item>
                    )}
                />
            </InfiniteScroll>
        </div>;

    return (
        <>
            <Descriptions
                bordered
                title={verification?.name}
            >
                <Descriptions.Item label="Certification Id">{verification?.certificationNumber}</Descriptions.Item>
                <Descriptions.Item label="Type">{verification?.type}</Descriptions.Item>
                <Descriptions.Item
                    label="Creator">{VerificationUtil.getCreator(verification)}</Descriptions.Item>
                <Descriptions.Item label="Create Time">{verification?.createTime}</Descriptions.Item>
                <Descriptions.Item label="Update Time"
                                   span={2}>{VerificationUtil.getLastUpdateTime(verification)}</Descriptions.Item>
                <Descriptions.Item label="Status">
                    <Badge status={VerificationUtil.getItemStatus(verification?.status)} text={verification?.status}/>
                </Descriptions.Item>
                <Descriptions.Item label="Approve" span={2}>
                    <Popover content={popContent}>{VerificationUtil.getApproveProportion(verification)}</Popover>
                </Descriptions.Item>
                <Descriptions.Item label="History" span={3}>
                    <Steps
                        type={"inline"}
                        items={VerificationUtil.getSteps(verification)}
                    />
                </Descriptions.Item>
                <Descriptions.Item
                    span={1}
                    label="Block Number">{verification?.blockNumber ? verification.blockNumber : "\\"}</Descriptions.Item>
                <Descriptions.Item span={2}
                                   label="Tx Hash">{verification?.txHash ? verification.txHash : "\\"}</Descriptions.Item>
                <Descriptions.Item label="Operations" span={3}>
                    <Space>
                        {
                            VerificationUtil.ifNeedToOperate(verification)
                            &&
                            <>
                                <Button
                                    size={"small"}
                                    type={"primary"}
                                    icon={<CheckOutlined/>}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setModalContent(
                                            <RelatedPartyOperation
                                                verificationId={id}
                                                hash={verification.hash}
                                                type={verification?.type}
                                                handleSuccess={() => {
                                                    setModalShowState(false);
                                                    setModalKey(modalKey + 1);
                                                    ModalUtil.show();
                                                }}
                                            />
                                        );
                                        setModalShowState(true);
                                    }}
                                >Approve</Button>
                                <Popconfirm
                                    title={"Reject this verification"}
                                    description={"It will make this process stop right now."}
                                    onConfirm={async (e) => {
                                        e.stopPropagation();
                                        let response = await VerificationService.relatedPartyOperate(id);
                                        if ("true" === response) {
                                            MessageUtil.success("Operation successes!");
                                            ModalUtil.show();
                                        } else if ("false" === response) {
                                            MessageUtil.error("Operation fails!");
                                        }
                                    }}
                                    onCancel={(e) => {
                                        e.stopPropagation();
                                    }}
                                    okText="Reject"
                                    cancelText="Back"
                                >
                                    <Button
                                        size={"small"}
                                        type={"primary"}
                                        icon={<CloseOutlined/>}
                                        danger
                                        onClick={() => {
                                        }}
                                    >
                                        Reject</Button>
                                </Popconfirm>
                            </>
                        }
                        {
                            verification?.status === "APPROVED"
                                ? <Button
                                    icon={<SearchOutlined/>}
                                    onClick={() => {
                                        setModalContent(
                                            <CheckWithId
                                                certificationNumber={verification?.certificationNumber}
                                                type={verification?.type}
                                            />
                                        );
                                        setModalShowState(true);
                                    }}
                                >Check</Button>
                                : verification?.status === "REJECTED" ? "No operation can do" : "Waiting for operating"
                        }
                    </Space>
                </Descriptions.Item>
            </Descriptions>
            <Modal
                width={"20%"}
                key={modalKey}
                open={modalShowState}
                onOk={() => setModalShowState(false)}
                onCancel={() => setModalShowState(false)}
                footer={null}
            >
                {modalContent}
            </Modal>
        </>
    );
}