import Login from "../component/account/Login";
import {useState} from "react";
import Header from "../component/common/Header";
import Section from "../component/common/Section";
import {LoginFunctionContext} from "../context/LoginFunctionContext";

export default function LoginPage() {
    const loginContent = <Login/>;
    const [sectionContent, setSectionContent] = useState(loginContent);

    const functions = {handlerContentChange};
    function handlerContentChange(content) {
        setSectionContent(content);
    }

    return (
        <>
            <Header/>
            <Section>
                <LoginFunctionContext.Provider value={functions}>
                    {sectionContent}
                </LoginFunctionContext.Provider>
            </Section>
        </>
    );
}