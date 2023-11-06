import {Button, Space} from "antd";
import {LeftOutlined} from "@ant-design/icons";
import {useContext} from "react";
import {LoginFunctionContext} from "../../context/LoginFunctionContext";
import AccountResult from "./AccountResult";
import ImportAccount from "./ImportAccount";
import Login from "./Login";
import AccountUtil from "../../utils/AccountUtil";

export default function SignUp() {
    let functions = useContext(LoginFunctionContext);

    async function handleClickGenerateAccount() {
        let web3Account = AccountUtil.generateAccount();
        let keyPair = {
            address: web3Account.address,
            privateKey: web3Account.privateKey
        }

        const generateResult = <AccountResult {...keyPair} />
        functions.handlerContentChange(generateResult);
    }

    function handleClickImportAccount() {
        const generateResult = <ImportAccount/>
        functions.handlerContentChange(generateResult);
    }

    return (
        <div className={"normal-content-wrapper"}>
            <Button
                className={"back-button"}
                type={"text"}
                size={"large"}
                onClick={() => functions.handlerContentChange(<Login/>)}
            >
                <LeftOutlined/>Back
            </Button>
            <Space
                style={{margin: "auto"}}
                direction={"vertical"}
                align={"center"}
            >
                <Button
                    className={"primary-button"}
                    type={"primary"}
                    onClick={() => handleClickGenerateAccount()}
                >
                    Generate account
                </Button>
                <Button
                    className={"primary-button"}
                    type={"dashed"}
                    onClick={() => handleClickImportAccount()}
                >
                    Import account
                </Button>
            </Space>
        </div>
    )
}