import LocalStorageConstant from "../constants/LocalStorageConstant";
import {KJUR} from "jsrsasign";
import {Web3} from "web3";
import BlockchainConstant from "../constants/BlockchainConstant";
import MessageUtil from "./MessageUtil";


class AccountUtil {

    web3Client;
    account;
    remarks;

    constructor() {
        this.web3Client = new Web3(BlockchainConstant.NETWORK);
    }

    getAccountByPrivateKey(privateKey) {
        return this.web3Client.eth.accounts.privateKeyToAccount(privateKey).address.toLowerCase();
    }

    init() {
        if (!this.web3Client) {
            this.web3Client = new Web3(BlockchainConstant.NETWORK);
        }
        if (!this.account) {
            let privateKey = localStorage.getItem(LocalStorageConstant.PRIVATE_KEY);
            if (privateKey) {
                try {
                    this.account = this.web3Client.eth.accounts.privateKeyToAccount(privateKey);
                } catch (e) {
                    this.resetAccount();
                    throw e;
                }
            } else {
                MessageUtil.error("Login status invalid, need login again!");
                window.location.reload();
            }

        }
    }

    getAddress() {
        return this.account?.address.toLowerCase();
    }

    generateAccount() {
        return this.web3Client.eth.accounts.create();
    }

    resetAccount() {
        this.account = null;
        localStorage.removeItem(LocalStorageConstant.PRIVATE_KEY);
    }

    sign(content) {
        this.init();
        return this.account.sign(content).signature;
    }

    verify(signature, message) {
        let address = this.web3Client.eth.accounts.recover(message, signature);
        return BlockchainConstant.BACKEND_ADDRESS === address.toLowerCase();
    }

    sha256(type, data) {
        return new Promise((resolve, reject) => {
            if (type !== "FILE") {
                //calculate hash of text
                resolve(KJUR.crypto.Util.sha256(data));
            } else {
                //calculate hash of file

                //set file reader
                const reader = new FileReader();
                reader.onload = function () {
                    const buffer = reader.result;
                    crypto.subtle.digest('SHA-256', buffer)
                        .then((hashBuffer) => {
                            //convert to hex
                            const hashArray = Array.from(new Uint8Array(hashBuffer));
                            const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
                            resolve(hashHex);
                        })
                        .catch(reject);
                };
                reader.onerror = function (event) {
                    reject(event.target.error);
                };

                //read file
                reader.readAsArrayBuffer(data);
            }
        });
    }

    getRemarkDisplay(address) {
        if (address === this.getAddress()) {
            return `(Me) ${address}`;
        }

        for (let remark of this.remarks) {
            let value = remark.toAddress;
            let label = remark.remark;

            if (value === address) {
                return `(${label}) ${value}`
            }
        }
        return address;
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new AccountUtil();