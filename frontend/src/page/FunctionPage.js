import "./css/FunctionPage.css"

import Header from "../component/common/Header";
import Section from "../component/common/Section";
import {Menu} from "antd";
import {
    AreaChartOutlined, ContactsOutlined,
    FileAddOutlined, FolderOutlined
} from "@ant-design/icons";
import {useEffect, useState} from "react";
import {Content} from "antd/es/layout/layout";
import Portal from "../component/function/Portal";
import SetVerification from "../component/function/SetVerification";
import Verifications from "../component/function/Verifications";
import AccountUtil from "../utils/AccountUtil";
import Remarks from "../component/function/Remarks";
import RemarkService from "../service/RemarkService";

function getItem(label, key, icon, onClick) {
    return {
        key,
        icon,
        label,
        onClick
    };
}

export default function FunctionPage() {
    //init
    AccountUtil.init();

    const [sectionContent, setSectionContent] = useState(<Portal/>);
    const [contentWidth, setContentWidth] = useState("40vw");

    useEffect(() => {
        RemarkService.list()
            .then(result => AccountUtil.remarks = result);
    }, [])

    const items = [
        getItem('Index', 'index', <AreaChartOutlined/>, () => {
            setContentWidth("40vw");
            setSectionContent(<Portal/>);
        }),
        getItem('Set', 'set', <FileAddOutlined/>, () => {
            setContentWidth("30vw");
            setSectionContent(<SetVerification/>);
        }),
        getItem('Documents', 'documents', <FolderOutlined/>, () => {
            setContentWidth("58vw");
            setSectionContent(<Verifications/>)
        }),
        getItem('Remarks', 'remarks', <ContactsOutlined/>, () => {
            setContentWidth("38vw");
            setSectionContent(<Remarks/>)
        }),
    ]

    return (
        <>
            <Header/>
            <Section>
                <div
                    className={"function-page-layout"}
                >
                    <Menu
                        defaultSelectedKeys={["index"]}
                        items={items}
                    />
                    <Content
                        style={{width: contentWidth}}
                        className={"function-page-content"}
                    >
                        {sectionContent}
                    </Content>
                </div>
            </Section>
        </>
    );
}