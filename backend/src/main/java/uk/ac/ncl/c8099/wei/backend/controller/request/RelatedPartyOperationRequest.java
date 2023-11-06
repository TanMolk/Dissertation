package uk.ac.ncl.c8099.wei.backend.controller.request;

import lombok.Data;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

/**
 * @author wei tan
 */

@Data
public class RelatedPartyOperationRequest {
    private Integer verificationId;
    private VerificationStatusEnum status;
    private String calculateResult;
}
