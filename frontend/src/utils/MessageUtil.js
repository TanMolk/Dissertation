class MessageUtil {
    messageApi;

    init(api) {
        this.messageApi = api;
    }

    success(msg) {
        this.messageApi.success(msg);
    }

    warn(msg){
        this.messageApi.warning(msg);
    }

    error(msg){
        this.messageApi.error(msg);
    }
}

export default new MessageUtil();