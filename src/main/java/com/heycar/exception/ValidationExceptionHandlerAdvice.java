package com.heycar.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(basePackages = "com.heycar")
@Slf4j
public class ValidationExceptionHandlerAdvice {

    @ExceptionHandler({CodeDefinedException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handle(CodeDefinedException exception) {
        log.info("Validation error - ", exception); // Validation errors are not actual errors so info is just fine.
        ResponseStatus annotation = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        HttpStatus status = getStatus(annotation);
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", exception.getMessage());
        body.put("error", exception.getClass());
        return new ResponseEntity(body, status);
    }

    private HttpStatus getStatus(ResponseStatus annotation) {
        HttpStatus resultStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (annotation != null) {
            Map<String, Object> attr = AnnotationUtils.getAnnotationAttributes(annotation);
            resultStatus = (HttpStatus)attr.get("value");
        } else {
            log.error("ResponseCode is not defined, sending HTTP 500 back.");
        }

        return resultStatus;
    }
}
