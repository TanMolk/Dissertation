import BaseService from "./BaseService";
import AccountUtil from "../utils/AccountUtil";

class RemarkService extends BaseService {

    /**
     * Add remark to an address
     * @param to the aim address
     * @param remark the content of remark
     */
    async add(to, remark) {
        //crate an object to wrap request params
        let data = {to, remark};
        //convert this data object to json string
        let dataStr = JSON.stringify(data);

        //request the api
        return await fetch(this.gateway + "/remark/add",
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

    async update(id, remark) {
        let data = {id, remark};
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/remark/update",
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

    async remove(id) {
        let data = {id};
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/remark/remove",
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
            });
    }

    async list() {
        let data = {};
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/remark/list",
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
export default new RemarkService();