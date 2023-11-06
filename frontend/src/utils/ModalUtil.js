class ModalUtil {
    modal;

    changeContent;
    changeShowSate;

    changeWidth;

    show() {
        this.changeShowSate(true);
    }

    hide() {
        this.changeShowSate(false);
    }

    setContent(content, width) {
        this.changeContent(content);
        if (width) {
            this.changeWidth(width);
        }
    }
}

export default new ModalUtil();