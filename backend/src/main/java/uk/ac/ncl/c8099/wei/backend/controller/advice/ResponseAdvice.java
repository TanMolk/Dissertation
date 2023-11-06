package uk.ac.ncl.c8099.wei.backend.controller.advice;

import jakarta.annotation.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.controller.response.GeneralResponse;
import uk.ac.ncl.c8099.wei.backend.service.AccountService;
import uk.ac.ncl.c8099.wei.backend.utils.JsonUtil;

/**
 * @author wei tan
 */

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private AccountService accountService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        try {
            //sign to return data
            String signature = accountService.sign(body);

            //create a general response
            GeneralResponse responseObj = new GeneralResponse();
            responseObj.setData(JsonUtil.toJsonStr(body));
            responseObj.setSignature(signature);


            AddressContext.clean();
            return responseObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
