import './App.css';

import LoginPage from "./page/LoginPage";
import FunctionPage from "./page/FunctionPage";
import {message, Modal} from "antd";
import MessageUtil from "./utils/MessageUtil";
import {useState} from "react";
import ModalUtil from "./utils/ModalUtil";

function App({isLogin}) {
    const [messageApi, contextHolder] = message.useMessage();
    const [modalContent, setModalContent] = useState(null);
    const [modalKey, setModalKey] = useState(0);
    const [modalShowState, setModalShowState] = useState(false);
    const [modalWidth, setModalWidth] = useState("80%");

    //init messageUtil
    MessageUtil.init(messageApi);
    //init Modal window
    ModalUtil.changeContent = setModalContent;
    ModalUtil.changeShowSate = (val) => {
        setModalShowState(val);
        if (val) {
            setModalKey(modalKey + 1);
        }
    };
    ModalUtil.changeWidth = setModalWidth;

    return (
        <>
            {contextHolder}
            <Modal
                key={modalKey}
                width={modalWidth}
                open={modalShowState}
                onOk={() => setModalShowState(false)}
                onCancel={() => setModalShowState(false)}
                footer={null}
            >
                {modalContent}
            </Modal>
            {isLogin ? <FunctionPage/> : <LoginPage/>}
        </>
    );
}

export default App;
