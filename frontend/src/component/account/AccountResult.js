import {Button, QRCode, Result, Space, Typography} from "antd";
import AccountService from "../../service/AccountService";
import LocalStorageConstant from "../../constants/LocalStorageConstant";
import MessageUtil from "../../utils/MessageUtil";
import ModalUtil from "../../utils/ModalUtil";
import SignUp from "./SignUp";
import {LeftOutlined} from "@ant-design/icons";
import {useContext} from "react";
import {LoginFunctionContext} from "../../context/LoginFunctionContext";

export default function AccountResult({isFromImport = false, address, privateKey}) {

    let functions = useContext(LoginFunctionContext);

    async function handleSignUp() {
        let response = await AccountService.signUp(address, privateKey);
        if (response === "true") {
            localStorage.setItem(LocalStorageConstant.PRIVATE_KEY, privateKey);
            MessageUtil.success("Login success");
        } else if (response === "false") {
            MessageUtil.error("This account exists");
        }
        setTimeout(() => {
            window.location.reload();
        }, 500);
    }

    const downloadQRCode = () => {
        const qrCodeCanvas = document.getElementById(address)?.querySelector('canvas');

        if (qrCodeCanvas) {

            //draw new qrCode, make background as white
            const newCanvas = document.createElement('canvas');
            const newContext = newCanvas.getContext('2d');
            newCanvas.width = qrCodeCanvas.width;
            newCanvas.height = qrCodeCanvas.height;
            newContext.fillStyle = '#fff';
            newContext.fillRect(0, 0, qrCodeCanvas.width, qrCodeCanvas.height);

            let destinationImage = new Image();

            //set callback
            destinationImage.onload = function () {
                newContext.drawImage(destinationImage, 0, 0);

                //download
                const a = document.createElement('a');
                a.download = `${address}.PNG`;
                a.href = newCanvas.toDataURL();
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            };

            //transfer and download
            destinationImage.src = qrCodeCanvas.toDataURL();
        }
    };

    return (
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
            <Space
                direction={"vertical"}
                align={"center"}
                style={{margin: "auto"}}
            >
                <div>
                    <p>Click QR code to download</p>
                    {!isFromImport && <p>Click private key to copy it</p>}
                </div>
                <div
                    id={address}
                    onClick={() => downloadQRCode()}
                >
                    <QRCode
                        size={240}
                        value={privateKey}
                    />
                </div>
                {!isFromImport &&
                    <Typography.Text
                        style={
                            {
                                width: "20ch",
                            }}
                        ellipsis={
                            {
                                tooltip: privateKey,
                            }}
                        copyable
                    >
                        {privateKey}
                    </Typography.Text>
                }
                <Button
                    type={"primary"}
                    className={"primary-button"}
                    onClick={() => {
                        ModalUtil.setContent(<Result
                            status="info"
                            title="Make sure you have stored"
                            subTitle={"This page won't show anymore! " +
                                "If you don't save your private key, you will lose this account forever!"}
                            extra={[
                                <Button
                                    key="sure"
                                    type="primary"
                                    onClick={async () => {
                                        ModalUtil.hide();
                                        await handleSignUp();
                                    }}
                                >
                                    I'm sure
                                </Button>,
                                <Button
                                    key="back"
                                    onClick={() => ModalUtil.hide()}
                                >
                                    Back
                                </Button>,
                            ]}
                        />);
                        ModalUtil.show();
                    }}
                >I have stored these secrets</Button>
            </Space>
        </div>
    );
}