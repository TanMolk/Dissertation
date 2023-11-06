import {Button, Form, Input, Radio, Result, Typography} from "antd";
import {useState} from "react";
import DocumentContent from "./DocumentContent";
import AccountUtil from "../../utils/AccountUtil";
import VerificationService from "../../service/VerificationService";
import ModalUtil from "../../utils/ModalUtil";

export default function Check() {
    const [form] = Form.useForm();
    const [buttonLoading, setButtonLoading] = useState(false);
    const [documentType, setDocumentType] = useState("TEXT");
    const [calculateFile, setCalculateFile] = useState(null);

    async function handleSuccessSubmit(data) {
        setButtonLoading(true);
        let sha256Data = data.type === "TEXT" ? data.content : calculateFile;
        let sha256 = await AccountUtil.sha256(data.type, sha256Data);

        let response = await VerificationService.check(data.certificationNumber, sha256);
        setButtonLoading(false);

        if ("true" === response) {
            ModalUtil.setContent(
                <Result
                    status="success"
                    title="File integrity is good"
                    subTitle={
                        <>
                            <Typography.Paragraph>By checking the data in the blockchain,
                                your document is the same as the original document.</Typography.Paragraph>
                            <Typography.Paragraph>The sha256 is {sha256}</Typography.Paragraph>
                        </>
                    }
                    extra={[
                        <Button type="primary"
                                key="console"
                                onClick={() => ModalUtil.hide()}
                        >
                            Get it!
                        </Button>,
                    ]}
                />, "50%");
            form.resetFields();
            setDocumentType("TEXT");

        } else {
            ModalUtil.setContent(
                <Result
                    status="error"
                    title="File integrity is bad"
                    subTitle={`
                    By checking with the data in the blockchain, 
                    your document is different from the original document.`}
                    extra={[
                        <Button type="primary"
                                key="console"
                                onClick={() => ModalUtil.hide()}
                        >
                            Try other
                        </Button>,
                    ]}
                />, "50%");
        }

        ModalUtil.show();
    }

    return (
        <Form
            form={form}
            style={{margin: "auto"}}
            layout={"vertical"}
            onFinish={handleSuccessSubmit}
        >
            <Form.Item
                label={"Certification Number"}
                name={"certificationNumber"}
                rules={[
                    {
                        required: true,
                        message: "This item can't be empty!",
                    }
                ]}
            >
                <Input/>
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
            <Form.Item>
                <Button
                    type={"primary"}
                    htmlType={"submit"}
                    loading={buttonLoading}
                >Verify Integrity</Button>
            </Form.Item>
        </Form>
    );
}