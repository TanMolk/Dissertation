import BaseService from "./BaseService";
import AccountUtil from "../utils/AccountUtil";
import LocalStorageConstant from "../constants/LocalStorageConstant";
import MessageUtil from "../utils/MessageUtil";

class AccountService extends BaseService {
    async signUp(address, privateKey) {
        localStorage.setItem(LocalStorageConstant.PRIVATE_KEY, privateKey);

        try {
            let data = {
                cipher: "signUp",
            }
            let dataStr = JSON.stringify(data);

            return await fetch(this.gateway + "/account/sign-up",
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    method: 'POST',
                    body: JSON.stringify({
                        account: address,
                        data: dataStr,
                        signature: AccountUtil.sign(dataStr)
                    })
                })
                .then(response => super.handeResponse(response, true));
        } catch (e) {
            AccountUtil.resetAccount();
            console.error(e);
            MessageUtil.error("Network error");
        }
    }

    async login(privateKey) {
        localStorage.setItem(LocalStorageConstant.PRIVATE_KEY, privateKey);
        AccountUtil.init();

        let data = {
            cipher: "login",
        }
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/account/login",
            {
                headers: {
                    'Content-Type': 'application/json',
                },
                method: 'POST',
                body: JSON.stringify({
                    account: AccountUtil.getAddress(),
                    data: dataStr,
                    signature: AccountUtil.sign(dataStr)
                })
            })
            .then(response => super.handeResponse(response, true));
    }

    async list() {
        let data = {}
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/account/list",
            {
                headers: {
                    'Content-Type': 'application/json',
                },
                method: 'POST',
                body: JSON.stringify({
                    account: AccountUtil.getAddress(),
                    data: dataStr,
                    signature: AccountUtil.sign(dataStr)
                })
            })
            .then(response => super.handeResponse(response));
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new AccountService();