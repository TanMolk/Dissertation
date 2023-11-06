import {Button, Form} from "antd";
import DocumentContent from "./DocumentContent";
import {useState} from "react";
import VerificationService from "../../service/VerificationService";
import MessageUtil from "../../utils/MessageUtil";
import AccountUtil from "../../utils/AccountUtil";
import UpdateComponentUtil from "../../utils/UpdateComponentUtil";

export default function RelatedPartyOperation({verificationId, hash, type, handleSuccess}) {
    const [form] = Form.useForm();
    const [buttonLoading, setButtonLoading] = useState(false);
    const [calculateFile, setCalculateFile] = useState(null);

    async function handleSuccessSubmit(data) {
        setButtonLoading(true);

        let sha256Data = (type === "TEXT" ? data.content : calculateFile);
        let sha256 = await AccountUtil.sha256(type, sha256Data);

        if (sha256 === hash) {
            let response = await VerificationService.relatedPartyOperate(verificationId, sha256);

            if ("true" === response) {
                MessageUtil.success("Operation successes!");
                form.resetFields();
                handleSuccess();
                UpdateComponentUtil.updateList();
            } else if ("false" === response) {
                MessageUtil.error("Operation fails!")
            }
        } else {
            MessageUtil.warn("This content is not the same as the original one!");
        }
        setButtonLoading(false);
    }

    return (
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
                >Approve</Button>
            </Form.Item>
        </Form>
    );
}