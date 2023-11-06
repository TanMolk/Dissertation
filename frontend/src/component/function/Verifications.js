import {Badge, Button, Divider, List, Popconfirm, Space, Statistic} from "antd";
import {useEffect, useState} from "react";
import VerificationService from "../../service/VerificationService";
import MessageUtil from "../../utils/MessageUtil";
import InfiniteScroll from "react-infinite-scroll-component";
import VerificationUtil from "../../utils/VerificationUtil";
import ModalUtil from "../../utils/ModalUtil";
import VerificationInfo from "./VerificationInfo";
import {CheckOutlined, CloseOutlined} from "@ant-design/icons";
import UpdateComponentUtil from "../../utils/UpdateComponentUtil";
import RelatedPartyOperation from "./RelatedPartyOperation";

export default function Verifications() {

    const [verifications, setVerifications] = useState([]);

    UpdateComponentUtil.updateList = () => {
        updateVerifications();
    }

    const showVerificationInfo = (id) => {
        ModalUtil.setContent(<VerificationInfo id={id}/>, "80%");
        ModalUtil.show();
    }

    async function updateVerifications() {
        try {
            let verifications = await VerificationService.list();
            setVerifications(verifications);
        } catch (e) {
            console.error(e);
            MessageUtil.error("Acquire verification list fails!");
        }
    }

    useEffect(() => {
        updateVerifications();

    }, []);

    return (
        <div
            id="verifications-list"
            style={{
                height: "70vh",
                overflow: 'auto',
                padding: '0 16px',
            }}
        >
            <InfiniteScroll
                dataLength={verifications.length}
                next={() => {
                }}
                hasMore={false}
                loader={null}
                endMessage={<Divider plain>No more</Divider>}
                scrollableTarget="verifications-list"
            >
                <List
                    style={{margin: "auto", width: "55.5vw"}}
                    size="large"
                    header={null}
                    footer={null}
                    bordered
                    dataSource={verifications}
                    renderItem={(item) =>
                        <List.Item
                            key={item.id}
                            onClick={() => showVerificationInfo(item.id)}
                        >
                            <List.Item.Meta
                                title={item.name}
                                description={
                                    VerificationUtil.ifNeedToOperate(item)
                                    &&
                                    <>
                                        <Space>
                                            <Button
                                                size={"small"}
                                                type={"primary"}
                                                icon={<CheckOutlined/>}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    ModalUtil.setContent(
                                                        <RelatedPartyOperation
                                                            verificationId={item.id}
                                                            hash={item.hash}
                                                            type={item.type}
                                                            handleSuccess={() => {
                                                                updateVerifications();
                                                                ModalUtil.hide();
                                                            }}
                                                        />, "20%");
                                                    ModalUtil.show();
                                                }}
                                            >Approve</Button>
                                            <Popconfirm
                                                title={"Reject this verification"}
                                                description={"It will make this process stop right now."}
                                                onConfirm={async (e) => {
                                                    e.stopPropagation();
                                                    let response = await VerificationService.relatedPartyOperate(item.id);
                                                    if ("true" === response) {
                                                        MessageUtil.success("Operation successes!");
                                                        updateVerifications();
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
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                    }}
                                                >
                                                    Reject</Button>
                                            </Popconfirm>
                                        </Space>
                                    </>
                                }
                            />
                            <Space size={32}>
                                <Statistic title={"Type"} value={item.type}/>
                                <Statistic title={"Create Time"} value={item.createTime}/>
                                <Statistic title={"Approve"}
                                           value={VerificationUtil.getApproveProportion(item)}/>
                                <Badge status={VerificationUtil.getItemStatus(item.status)} text={item.status}/>
                            </Space>
                        </List.Item>}
                />
            </InfiniteScroll>
        </div>
    );
}