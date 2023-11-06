package uk.ac.ncl.c8099.wei.backend.controller.request;

import lombok.Data;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationTypeEnum;

import java.util.List;

/**
 * @author wei tan
 */

@Data
public class SetVerificationRequest {
    private String name;
    private VerificationTypeEnum type;
    private List<String> relatedParties;
    private String originalHash;
    private String calculateResult;
}
