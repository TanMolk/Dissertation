import BaseService from "./BaseService";
import AccountUtil from "../utils/AccountUtil";

class VerificationService extends BaseService {
    async set(srcData) {
        let data = srcData;
        data.calculateResult = AccountUtil.sign(data.originalHash);

        let creatorAddress = AccountUtil.getAddress();
        data.relatedParties.push(creatorAddress);
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/verification/set",
            {
                headers: {
                    'Content-Type': 'application/json',
                },
                method: 'POST',
                body: JSON.stringify({
                    account: creatorAddress,
                    data: dataStr,
                    signature: AccountUtil.sign(dataStr)
                })
            })
            .then(response => super.handeResponse(response, true));
    }

    async list() {
        let data = {};

        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/verification/list",
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

    async get(id) {
        let data = {id};
        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/verification/get",
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

    async relatedPartyOperate(verificationId, calculateResult) {
        let status = calculateResult ? "APPROVED" : "REJECTED"
        if (status === "APPROVED") {
            calculateResult = AccountUtil.sign(calculateResult);
        }
        let data = {verificationId, calculateResult, status};

        let dataStr = JSON.stringify(data);

        return await fetch(this.gateway + "/verification/related-party-operation",
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

    async check(certificationNumber, hash) {

        return await (await fetch(this.gateway + "/verification/check",
            {
                headers: {
                    'Content-Type': 'application/json',
                },
                method: 'POST',
                body: JSON.stringify({certificationNumber, hash})
            })
            .then(response => super.handeResponse(response, true)));
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new VerificationService();