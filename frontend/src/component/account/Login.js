import {Button, Divider, Space, Typography, Upload} from "antd";
import {PlusCircleOutlined, SearchOutlined} from "@ant-design/icons";
import "./css/LoginContent.css"
import {useContext} from "react";
import {LoginFunctionContext} from "../../context/LoginFunctionContext";
import SignUp from "./SignUp";
import LoginWithPrivateKey from "./LoginWithPrivateKey";
import AccountService from "../../service/AccountService";
import jsQR from "jsqr";
import MessageUtil from "../../utils/MessageUtil";
import AccountUtil from "../../utils/AccountUtil";
import Check from "../function/Check";
import ModalUtil from "../../utils/ModalUtil";

const handleQrCodeRecognize = async (file) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (event) => {
            const imageData = event.target.result;
            const img = new Image();
            img.src = imageData;
            img.onload = () => {
                const canvas = document.createElement('canvas');
                const context = canvas.getContext('2d');
                canvas.width = img.width;
                canvas.height = img.height;
                context.drawImage(img, 0, 0, img.width, img.height);
                const imageData = context.getImageData(0, 0, img.width, img.height);
                const code = jsQR(imageData.data, imageData.width, imageData.height);
                if (code) {
                    resolve(code.data);
                } else {
                    reject(new Error('No QR code found.'));
                }
            };
        };
        reader.onerror = (event) => {
            reject(event.target.error);
        };
        reader.readAsDataURL(file);
    });
};

export default function Login({ifMobile = false}) {
    let functions = useContext(LoginFunctionContext);

    async function handleQrCodeProviding(file) {
        try {
            let result = await handleQrCodeRecognize(file);
            let response = await AccountService.login(result);

            if ("true" === response) {
                MessageUtil.success("Login success!");
                setTimeout(() => {
                    window.location.reload();
                }, 500)
            } else {
                MessageUtil.warn("Login fails! Please check your account.");
                AccountUtil.resetAccount();
            }
        } catch (e) {
            if ("TypeError" === e.name) {
                MessageUtil.error("NetWork error");
            } else {
                MessageUtil.error("Can't read a right content from this picture!");
            }
            AccountUtil.resetAccount();
        }

        return false;
    }

    return (
        <div className={ifMobile ? "login-content-mobile" : "login-content-desktop"}>
            {!ifMobile &&
                <>
                    <div
                        style={{padding: "8px"}}
                    >
                        <Typography.Title level={2}>Consistent, easy and safe</Typography.Title>
                        <Typography.Paragraph>
                            This is a system to verify the integrity of files.
                        </Typography.Paragraph>
                        <Typography.Paragraph>
                            You can use this system to combine the ability of
                            blockchain to give proofs to your digital documents.
                        </Typography.Paragraph>
                        <Divider
                            style={
                                {
                                    marginTop: "13rem",
                                }
                            }
                            plain
                        ><i>Check your integrity</i></Divider>
                        <div style={{width: "100%", display: "block"}}>
                            <Button
                                style={{margin: "auto", display: "block"}}
                                icon={<SearchOutlined/>}
                                onClick={() => {
                                    ModalUtil.setContent(<Check/>, "20%");
                                    ModalUtil.show();
                                }}
                            >Check</Button>
                        </div>
                    </div>
                    <div className={"line"}/>
                </>}
            <Button
                className={"sign-up-button"}
                type={"text"}
                size={"large"}
                onClick={() => functions.handlerContentChange(<SignUp/>)}
            >
                Sign up<PlusCircleOutlined/>
            </Button>
            <div className={"login-buttons"}>
                <Space
                    direction={"vertical"}
                    align={"center"}
                >
                    {ifMobile
                        ? <Button
                            className={"primary-button"}
                            type={"primary"}
                        >
                            Sign in with QR code
                        </Button>
                        : <Upload
                            accept={"image/*"}
                            maxCount={1}
                            beforeUpload={handleQrCodeProviding}
                            itemRender={() => null}
                        >
                            <Button
                                className={"primary-button"}
                                type={"primary"}
                            >
                                Sign in with QR code
                            </Button>
                        </Upload>}

                    <Button
                        className={"primary-button"}
                        type={"text"}
                        style={{color: "#1890FF"}}
                        onClick={() => functions.handlerContentChange(<LoginWithPrivateKey/>)}
                    >
                        Enter private key to sign in
                    </Button>
                </Space>
            </div>
        </div>
    );
}