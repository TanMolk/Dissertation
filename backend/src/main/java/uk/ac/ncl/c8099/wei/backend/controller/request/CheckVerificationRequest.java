package uk.ac.ncl.c8099.wei.backend.controller.request;

import lombok.Data;

/**
 * @author wei tan
 */

@Data
public class CheckVerificationRequest {
    private Long certificationNumber;
    private String hash;
}
