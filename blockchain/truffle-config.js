

module.exports = {
    networks: {
        development: {
            host: "127.0.0.1",
            port: 8545,
            network_id: "*"
        }
    },
    contracts_directory: "./contracts",
    contracts_build_directory: "./output",

    // Configure your compilers
    compilers: {
        solc: {
            version: "0.8.12",
            optimizer: {
                enabled: true,
                runs: 200
            }
        }
    }
};
