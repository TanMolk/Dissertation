package uk.ac.ncl.c8099.wei.backend.controller.advice;

import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import uk.ac.ncl.c8099.wei.backend.controller.annotation.WithoutDecrypt;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.controller.request.GeneralRequest;
import uk.ac.ncl.c8099.wei.backend.service.AccountService;
import uk.ac.ncl.c8099.wei.backend.utils.JsonUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author wei tan
 */

@ControllerAdvice
public class RequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Resource
    private AccountService accountService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !methodParameter.hasMethodAnnotation(WithoutDecrypt.class);
    }

    @NotNull
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {

            String originalRequestBody = new String(httpInputMessage.getBody().readAllBytes());
            GeneralRequest requestBody = JsonUtil.jsonStrToObj(originalRequestBody, GeneralRequest.class);

            //set context
            AddressContext.set(requestBody.getAccount().toLowerCase());

            //check signature
            if (!accountService.verifySignature(requestBody.getSignature(),requestBody.getData())) {
                throw new RuntimeException("Verify data fails");
            }

            //get real data
            Map data = JsonUtil.jsonStrToObj(requestBody.getData(), Map.class);
            InputStream inputStream = new ByteArrayInputStream(JsonUtil.toJsonStr(data).getBytes());

            return new HttpInputMessage() {
                @NotNull
                @Override
                public InputStream getBody() {
                    return inputStream;
                }

                @NotNull
                @Override
                public HttpHeaders getHeaders() {
                    return httpInputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Decrypt fails");
        }
    }
}
