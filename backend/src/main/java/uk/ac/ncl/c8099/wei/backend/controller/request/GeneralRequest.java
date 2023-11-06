package uk.ac.ncl.c8099.wei.backend.controller.request;

import lombok.Data;

/**
 * @author wei tan
 */

@Data
public class GeneralRequest {
    private String account;
    private String data;
    private String signature;
}
