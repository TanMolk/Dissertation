const Token = artifacts.require("DocumentIntegrity");

module.exports = function (developer) {
    developer.deploy(Token);
}