import {Button, Form, Modal, Result, Typography} from "antd";
import {useState} from "react";
import DocumentContent from "./DocumentContent";
import AccountUtil from "../../utils/AccountUtil";
import VerificationService from "../../service/VerificationService";

export default function CheckWithId({certificationNumber, type}) {
    const [form] = Form.useForm();
    const [buttonLoading,setButtonLoading] = useState(false);
    const [calculateFile, setCalculateFile] = useState(null);
    const [modalShowState, setModalShowState] = useState(false);
    const [modalContent, setModalContent] = useState(null);

    async function handleSuccessSubmit(data) {
        setButtonLoading(true);

        let sha256Data = type === "TEXT" ? data.content : calculateFile;
        let sha256 = await AccountUtil.sha256(type, sha256Data);

        let response = await VerificationService.check(certificationNumber, sha256);
        setButtonLoading(false);

        if ("true" === response) {
            setModalContent(
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
                                onClick={() => setModalShowState(false)}
                        >
                            Get it!
                        </Button>,
                    ]}
                />);
            form.resetFields();

        } else {
            setModalContent(
                <Result
                    status="error"
                    title="File integrity is bad"
                    subTitle={`
                    By checking with the data in the blockchain, 
                    your document is different from the original document.`}
                    extra={[
                        <Button type="primary"
                                key="console"
                                onClick={() => setModalShowState(false)}
                        >
                            Try other
                        </Button>,
                    ]}
                />);
        }

        setModalShowState(true);
    }

    return (
        <>
            <Form
                form={form}
                style={{margin: "auto"}}
                layout={"vertical"}
                onFinish={handleSuccessSubmit}
            >
                <DocumentContent
                    ifText={type === "TEXT"}
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
            <Modal
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