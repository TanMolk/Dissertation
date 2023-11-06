import "./css/Header.css"
import AccountUtil from "../../utils/AccountUtil";
import {Typography} from "antd";

export default function Header() {
    return (
        <header>
            <div
                style={{
                    position: "relative",
                    display: "flex",
                    alignItems: "center",
                    margin: "auto",
                    height: "100%"
                }}
            >
                <img className={"header-logo"} src={"./logo.png"} alt={"LOGO"}/>
                {AccountUtil.getAddress() && <div
                    style={
                        {
                            position: "absolute",
                            right: "8px",
                            color: "white"
                        }}
                    onClick={() => {
                        AccountUtil.resetAccount();
                        window.location.reload();
                    }}
                >
                    <Typography.Text
                        style={
                            {
                                width: "20ch",
                                color: "white"
                            }}
                        ellipsis={
                            {
                                tooltip: AccountUtil.getAddress(),
                            }}
                    >
                        {AccountUtil.getAddress()}
                    </Typography.Text>
                </div>
                }
            </div>
        </header>
    );
}