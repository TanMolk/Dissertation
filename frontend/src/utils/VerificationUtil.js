import AccountUtil from "./AccountUtil";

class VerificationUtil {
    getItemStatus = (status) => {
        switch (status) {
            case "PENDING":
                return "processing";
            case "APPROVED":
                return "success";
            default:
                return "error";
        }
    }

    getApproveProportion = (v) => {
        if (!v) return;
        if (!v.relatedParties) return;
        return v.relatedParties.filter(p => p.status === "APPROVED").length + " / " + v.relatedParties.length;
    }

    getCreator = (v) => {
        if (!v) return;

        return AccountUtil.getRemarkDisplay(v.accountAddress);
    }

    getLastUpdateTime = (v) => {
        if (!v) return;
        if (!v.relatedParties) return;

        let newArr = [...v.relatedParties];
        newArr.sort((r1, r2) => {
            return (r1.updateTime <= r2.updateTime) ? 1 : -1;
        });

        return newArr[0].updateTime;
    }

    getSteps = (v) => {
        let result = [];
        if (!v) return result;
        if (!v.relatedParties) return result;

        let newArr = [...v.relatedParties];
        newArr.sort((r1, r2) => {
            if (r1.updateTime === r2.updateTime) {
                return r1.status >= r2.status ? 1 : -1;
            }

            return (r1.updateTime >= r2.updateTime) ? 1 : -1;
        });

        result.push({
            title: "Create",
            status: "finish",
            description: `${AccountUtil.getRemarkDisplay(newArr[0].partyAddress)}`
        });

        let pendingAmount = 0;
        let containReject = false;
        for (let i = 1; i < newArr.length; i++) {
            let relatedParty = newArr[i];
            if ("APPROVED" === relatedParty.status) {
                result.push({
                    title: `${relatedParty.status}`,
                    status: "finish",
                    description: `${AccountUtil.getRemarkDisplay(relatedParty.partyAddress)}`
                });
            } else if ("REJECTED" === relatedParty.status) {
                result.push({
                    title: `${relatedParty.status}`,
                    status: "finish",
                    description: `${AccountUtil.getRemarkDisplay(relatedParty.partyAddress)}`
                });
                containReject = true;
                break;
            } else {
                pendingAmount++;
            }
        }

        if (containReject) {
            result.push({
                title: `End`,
                status: "error",
                description: `This verification get rejected.`
            });
            return result;
        }

        if (pendingAmount === 0) {
            result.push({
                title: `Approve`,
                status: "finish",
                description: `This verification get approved.`
            });
        } else {
            result.push({
                title: `Waiting`,
                status: "process",
                description: `Waiting left ${pendingAmount} related parties to operate.`
            });
        }

        return result;
    }

    ifNeedToOperate = (v) => {
        if (!v) return;
        if (!v.relatedParties) return;

        return v.relatedParties.filter(r => r.partyAddress === AccountUtil.getAddress())[0].status === "PENDING"
            && v.status === "PENDING";
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new VerificationUtil();