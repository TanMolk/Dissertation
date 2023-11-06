import {Form, Upload} from "antd";
import TextArea from "antd/es/input/TextArea";
import {Button} from "antd/lib";
import {UploadOutlined} from "@ant-design/icons";
import {useState} from "react";


// From https://ant.design/components/form#components-form-demo-validate-other
const transferValue = (e) => {
    if (Array.isArray(e)) {
        return e;
    }
    return e?.fileList;
};

export default function DocumentContent({ifText = false,onFileUpload}) {

    const [textContent, setTextContent] = useState("");

    if (ifText) {
        return (
            <Form.Item
                label={"Content"}
                name={"content"}
                rules={[
                    {
                        required: true,
                        message: "Content can't be empty!",
                    }
                ]}
            ><TextArea
                style={{resize: 'none'}}
                rows={4}
                placeholder={"enter your content"}
                allowClear={true}
                value={textContent}
                onChange={e => setTextContent(e.target.value)}
            />
            </Form.Item>
        );
    } else {
        return (
            <Form.Item
                label={"Content"}
                name={"fileContent"}
                valuePropName="fileList"
                getValueFromEvent={transferValue}
                rules={[
                    {
                        required: true,
                        message: "Content can't be empty!",
                    }
                ]}
            >
                <Upload
                    name="logo"
                    maxCount={1}
                    beforeUpload={onFileUpload}
                >
                    <Button
                        icon={<UploadOutlined/>}>
                        Click to upload
                    </Button>
                </Upload>
            </Form.Item>
        );
    }
}