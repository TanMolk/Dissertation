import {Button, Form} from "antd";
import {LeftOutlined} from "@ant-design/icons";
import TextArea from "antd/es/input/TextArea";
import {useContext, useState} from "react";
import {LoginFunctionContext} from "../../context/LoginFunctionContext";
import Login from "./Login";
import AccountService from "../../service/AccountService";
import MessageUtil from "../../utils/MessageUtil";
import AccountUtil from "../../utils/AccountUtil";

export default function LoginWithPrivateKey() {
    let functions = useContext(LoginFunctionContext);
    let [privateKey, setPrivateKey] = useState("");

    async function handleSuccessSubmit(data) {
        try {
            let response = await AccountService.login(data.privateKey);
            if ("true" === response) {
                MessageUtil.success("Login success");
                setTimeout(() => {
                    window.location.reload();
                }, 500)
            } else {
                MessageUtil.warn(`Account ${AccountUtil.getAddress()} doesn't exist`);
                AccountUtil.resetAccount();
            }
        } catch (e) {
            console.log(e.name)
            console.error(e);
            if (e.name === "InvalidPrivateKeyError"
                || e.name === "Ga"
                || e.name === "PrivateKeyLengthError"
                || e.name === "Ka"
            ) {
                MessageUtil.error("Please enter a legal key!");
            } else {
                MessageUtil.error("NetWork error");
            }
            AccountUtil.resetAccount();
        }
    }

    return (
        <div
            className={"normal-content-wrapper"}
        >
            <Button
                className={"back-button"}
                type={"text"}
                size={"large"}
                onClick={() => functions.handlerContentChange(<Login/>)}
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
                        style={{width: "60%", height: "5rem"}}
                        type={"primary"}
                        htmlType={"submit"}
                    >Sign in</Button>
                </Form.Item>
            </Form>
        </div>
    );
}