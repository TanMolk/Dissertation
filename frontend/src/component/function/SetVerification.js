import {Button, Form, Input, Radio, Select} from "antd";
import {useEffect, useState} from "react";
import DocumentContent from "./DocumentContent";
import AccountService from "../../service/AccountService";
import AccountUtil from "../../utils/AccountUtil";
import VerificationService from "../../service/VerificationService";
import MessageUtil from "../../utils/MessageUtil";
import ModalUtil from "../../utils/ModalUtil";
import VerificationInfo from "./VerificationInfo";

export default function SetVerification() {
    const [form] = Form.useForm();
    const [buttonLoading, setButtonLoading] = useState(false);
    const [documentType, setDocumentType] = useState("TEXT");
    const [relatedPartyList, setRelatedPartyList] = useState([]);
    const [calculateFile, setCalculateFile] = useState(null);

    useEffect(() => {
        async function updateRelatedPartyList() {
            try {
                let relatedPartyList = await AccountService.list();

                for (let relatedParty of relatedPartyList) {
                    if (relatedParty.value !== relatedParty.label) {
                        relatedParty.label = `(${relatedParty.label}) ${relatedParty.value}`
                    }
                }

                setRelatedPartyList(relatedPartyList);
            } catch (e) {
                console.error(e)
                MessageUtil.error("Acquire related parties fails!");
            }
        }

        updateRelatedPartyList();

    }, []);

    async function handleSuccessSubmit(data) {
        setButtonLoading(true);

        let sha256Data = data.type === "TEXT" ? data.content : calculateFile;
        data.originalHash = await AccountUtil.sha256(data.type, sha256Data);

        if (!data.relatedParties) {
            data.relatedParties = [];
        }

        let response = await VerificationService.set(data);
        setButtonLoading(false);

        if (response) {
            MessageUtil.success("Create verification successfully!");
            ModalUtil.setContent(<VerificationInfo id={response}/>, "80%")
            ModalUtil.show();
            form.resetFields();
            setDocumentType("TEXT");

        } else {
            MessageUtil.error("Create verification fails!");
        }
    }

    return (
        <Form
            form={form}
            style={{margin: "auto"}}
            layout={"vertical"}
            onFinish={handleSuccessSubmit}
        >
            <Form.Item
                label={"Name"}
                name={"name"}
                rules={[
                    {
                        required: true,
                        message: "Name can't be empty!",
                    },
                    {
                        max: 16,
                        message: "Name can't be more than 16 char"
                    }
                ]}
            >
                <Input
                    showCount
                    maxLength={16}
                />
            </Form.Item>
            <Form.Item
                label={"Type"}
                name={"type"}
                initialValue={"TEXT"}
                rules={[
                    {
                        required: true,
                        message: "Type can't be empty!",
                    }
                ]}
            >
                <Radio.Group
                    value={documentType}
                    onChange={e => setDocumentType(e.target.value)}
                >
                    <Radio value={"TEXT"}>Text</Radio>
                    <Radio value={"FILE"}>File</Radio>
                </Radio.Group>
            </Form.Item>
            <DocumentContent
                ifText={documentType === "TEXT"}
                onFileUpload={(file) => {
                    setCalculateFile(file);
                    return false;
                }}
            />
            <Form.Item
                label={"related parties"}
                name={"relatedParties"}
            >
                <Select
                    mode={"tags"}
                    placeholder="select parties related to"
                    options={relatedPartyList}
                    maxTagCount={3}
                    maxTagTextLength={4}
                />

            </Form.Item>
            <Form.Item>
                <Button
                    type={"primary"}
                    htmlType={"submit"}
                    loading={buttonLoading}
                >Set this verification</Button>
            </Form.Item>
        </Form>
    );
}