import AccountUtil from "../utils/AccountUtil";
import MessageUtil from "../utils/MessageUtil";

export default class BaseService {
    constructor() {
        this.gateway = "http://localhost:8080"
    }

    async handeResponse(response, notJson) {
        if (response.ok) {
            let resp = await response.json();

            if (AccountUtil.verify(resp.signature, resp.data)) {
                return notJson ? resp.data : JSON.parse(resp.data);
            } else {
                MessageUtil.error("Valid data signature fails");

                return Promise.reject({
                    status: -1,
                    statusText: "Check signature fails"
                });
            }
        } else {
            AccountUtil.resetAccount();
            MessageUtil.error("Login status invalid, need login again!");
            return Promise.reject({
                status: response.status,
                statusText: response.statusText
            });
        }
    }
}