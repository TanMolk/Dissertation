import InfiniteScroll from "react-infinite-scroll-component";
import {Button, Divider, Form, Input, List, Modal, Popconfirm, Typography} from "antd";
import {useEffect, useState} from "react";
import RemarkService from "../../service/RemarkService";
import MessageUtil from "../../utils/MessageUtil";
import AccountService from "../../service/AccountService";
import {AutoComplete} from "antd/lib";
import AccountUtil from "../../utils/AccountUtil";

let accountAddressList;

export default function Remarks() {

    const [form] = Form.useForm();
    const [remarks, setRemarks] = useState([]);
    const [modalShowState, setModalShowState] = useState(false);

    const [filterAccountAddressList, setFilterAccountAddressList] = useState([]);


    const updateRemarks = async () => {
        try {
            let remarks = await RemarkService.list();
            setRemarks(remarks);
            AccountUtil.remarks = remarks;
        } catch (e) {
            console.error(e);
            MessageUtil.error("Acquire remark list fails!");
        }
    }

    const updateAddressList = async () => {
        try {
            let addressList = await AccountService.list();
            //filter that don't have label
            let withoutLabel = addressList
                .filter(a => a.label === a.value);

            accountAddressList = withoutLabel;
            setFilterAccountAddressList(withoutLabel);
        } catch (e) {
            console.error(e)
            MessageUtil.error("Acquire related parties fails!");
        }
    }

    const updateData = () => {
        updateRemarks();
        updateAddressList();
    }

    const handleAdd = async (data) => {
        try {
            let result = await RemarkService.add(data.to, data.remark);
            if ("true" === result) {
                MessageUtil.success("Add remark successes!");
                updateData();
            } else {
                MessageUtil.error("Add remark fails!");
            }
        } catch (e) {
            console.error(e)
            MessageUtil.error("Network error!");
        }
        form.resetFields();
        setModalShowState(false);
    }

    const handleDelete = async (id) => {
        try {
            let response = await RemarkService.remove(id);
            if (response.ok) {
                MessageUtil.success("Request has been sent!");
                updateData();
            }
        } catch (e) {
            console.error(e)
            MessageUtil.error("Network error!");
        }
    }

    const handleUpdate = async (id, text) => {
        try {
            let result = await RemarkService.update(id, text);
            if ("true" === result) {
                MessageUtil.success("Change remark successes!");
                updateData();
            } else {
                MessageUtil.error("Add remark fails!");
            }
        } catch (e) {
            console.error(e)
            MessageUtil.error("Network error!");
        }
    }

    useEffect(() => {
        updateData();
    }, [])

    return (
        <>
            <div
                id="verifications-list"
                style={{
                    height: "70vh",
                    overflow: 'auto',
                    padding: '0 16px',
                }}
            >
                <InfiniteScroll
                    dataLength={remarks.length}
                    next={() => {
                    }}
                    hasMore={false}
                    loader={null}
                    endMessage={<Divider plain>No more</Divider>}
                    scrollableTarget="verifications-list"
                >
                    <List
                        style={{margin: "auto", width: "35vw"}}
                        size="large"
                        header={<>
                            <Button
                                type={"primary"}
                                onClick={() => {
                                    setModalShowState(true);
                                }}
                            >Add</Button>
                        </>}
                        footer={null}
                        bordered
                        dataSource={remarks}
                        renderItem={(item) =>
                            <List.Item
                                key={item.id}
                                onClick={() => {
                                }}
                            >
                                <List.Item.Meta
                                    title={<Typography.Text
                                        editable={{
                                            onChange: (text) => {
                                                if (text !== item.remark) {
                                                    handleUpdate(item.id, text);
                                                }
                                            },
                                        }}
                                    >
                                        {item.remark}</Typography.Text>}
                                    description={item.toAddress}
                                />
                                <Popconfirm
                                    title={"Remove this remark"}
                                    description={"Are you sure to delete this remark?"}
                                    onConfirm={() => handleDelete(item.id)}
                                    okText="OK"
                                    cancelText="Back"
                                >
                                    <Button
                                        type={"primary"}
                                        danger
                                    >
                                        Delete</Button>
                                </Popconfirm>
                            </List.Item>}
                    />
                </InfiniteScroll>
            </div>
            <Modal
                width={"20%"}
                open={modalShowState}
                onOk={() => setModalShowState(false)}
                onCancel={() => setModalShowState(false)}
                footer={null}
            >
                <Form
                    form={form}
                    layout={"vertical"}
                    onFinish={handleAdd}
                >
                    <Form.Item
                        label={"Address"}
                        name={"to"}
                        rules={[
                            {
                                required: true,
                                message: "Address can't be empty!",
                            }
                        ]}
                    >
                        <AutoComplete
                            style={{
                                width: "100%"
                            }}
                            options={filterAccountAddressList}
                            onSearch={(text) => {
                                let filterArr = accountAddressList.filter(adr => adr.label.includes(text));
                                setFilterAccountAddressList(filterArr);
                            }}
                        />
                    </Form.Item>
                    <Form.Item
                        label={"Remark"}
                        name={"remark"}
                        rules={[
                            {
                                required: true,
                                message: "Remark can't be empty!",
                            }
                        ]}
                    >
                        <Input
                            showCount
                            maxLength={32}
                        />
                    </Form.Item>
                    <Form.Item>
                        <Button
                            style={{margin: "auto", display: "block"}}
                            type={"primary"}
                            htmlType={"submit"}
                        >Add remark</Button>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
}