// SPDX-License-Identifier: MIT
pragma solidity ^0.8.12;

contract DocumentIntegrity {

    event AddCertificationEvent(uint48 indexed id);
    event AddCertificationsEvent(uint48[] indexed ids);

    mapping(uint48 => string) private certifications;

    function addCertification(uint48 id, string memory creatorSignature) public {
        require(bytes(certifications[id]).length == 0, "Already contains this certification");

        certifications[id] = creatorSignature;
        emit AddCertificationEvent(id);
    }

    function addCertifications(uint48[] memory idArr, string[] memory creatorSignatures) public {
        require(idArr.length == creatorSignatures.length, "Array lengths mismatch");

        for (uint256 i = 0; i < idArr.length; i++) {
            require(bytes(certifications[idArr[i]]).length == 0, "Already contains this certification");

            certifications[idArr[i]] = creatorSignatures[i];
            emit AddCertificationEvent(idArr[i]);
        }
    }

    function getCertification(uint48 id) public view returns (string memory) {
        string memory hash = certifications[id];
        return hash;
    }
}