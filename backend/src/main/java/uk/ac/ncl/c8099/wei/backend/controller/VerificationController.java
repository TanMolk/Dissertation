package uk.ac.ncl.c8099.wei.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.c8099.wei.backend.controller.annotation.WithoutDecrypt;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.controller.request.*;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.service.RelatedPartyService;
import uk.ac.ncl.c8099.wei.backend.service.VerificationService;

import java.util.List;

/**
 * @author wei tan
 */

@CrossOrigin
@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Resource
    private VerificationService verificationService;

    @Resource
    private RelatedPartyService relatedPartyService;

    @PostMapping("/set")
    public Integer set(@RequestBody SetVerificationRequest request) throws Exception {
        return verificationService.addVerification(
                request.getName(),
                request.getType(),
                AddressContext.get(),
                request.getRelatedParties(),
                request.getOriginalHash(),
                request.getCalculateResult()
        ).getId();
    }

    @WithoutDecrypt
    @PostMapping("/check")
    public Boolean check(@RequestBody CheckVerificationRequest request) throws Exception {
        return verificationService.checkDocumentIntegrity(request.getHash(), request.getCertificationNumber());
    }

    @PostMapping("/related-party-operation")
    public Boolean relatedPartyOperation(@RequestBody RelatedPartyOperationRequest request) throws Exception {
        return relatedPartyService.updateRelatedPartyState(
                request.getCalculateResult(),
                request.getStatus(),
                request.getVerificationId(),
                AddressContext.get());
    }

    @PostMapping("/list")
    public List<Verification> list(@RequestBody ListVerificationRequest request) {
        return verificationService.listAllVerificationByAddress(AddressContext.get());
    }

    @PostMapping("/get")
    public Verification get(@RequestBody GetVerificationRequest request) {
        return verificationService.queryVerification(request.getId());
    }
}
