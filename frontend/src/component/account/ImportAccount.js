import {Button, Form} from "antd";
import {useContext, useState} from "react";
import {LeftOutlined} from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import {LoginFunctionContext} from "../../context/LoginFunctionContext";
import AccountResult from "./AccountResult";
import SignUp from "./SignUp";
import AccountUtil from "../../utils/AccountUtil";
import MessageUtil from "../../utils/MessageUtil";

export default function ImportAccount() {
    let functions = useContext(LoginFunctionContext);

    let [privateKey, setPrivateKey] = useState("");

    async function handleSuccessSubmit(data) {
        try {
            let address = AccountUtil.getAccountByPrivateKey(data.privateKey);

            const generateResult = <AccountResult isFromImport={true}
                                                  address={address}
                                                  privateKey={data.privateKey}/>
            functions.handlerContentChange(generateResult);
        } catch (e) {
            console.error(e);
            if (e.name === "InvalidPrivateKeyError"
                || e.name === "Ga"
                || e.name === "PrivateKeyLengthError"
                || e.name === "Ka") {
                MessageUtil.error("Please enter a legal key!");
            } else {
                MessageUtil.error("NetWork error");
            }
            AccountUtil.resetAccount();
        }
    }

    return (
        <>
            <div
                className={"normal-content-wrapper"}
            >
                <Button
                    className={"back-button"}
                    type={"text"}
                    size={"large"}
                    onClick={() => functions.handlerContentChange(<SignUp/>)}
                >
                    <LeftOutlined/>Back
                </Button>
                <Form
                    style={{margin: "auto", width: "70%"}}
                    layout={"vertical"}
                    align={"center"}
                    onFinish={handleSuccessSubmit}
                >
                    <Form.Item
                        label={"Private Key"}
                        name={"privateKey"}
                        rules={[
                            {
                                required: true,
                                message: 'Please input private key!',
                            }
                        ]}
                    >
                        <TextArea
                            style={{resize: 'none'}}
                            rows={4}
                            placeholder={"enter your private key"}
                            allowClear={true}
                            value={privateKey}
                            onChange={e => setPrivateKey(e.target.value)}
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button
                            type={"primary"}
                            className={"primary-button"}
                            htmlType={"submit"}
                        >Import account</Button>
                    </Form.Item>
                </Form>
            </div>
        </>
    );
}