package org.saltaonelove.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ExceptionCodeMapper {

    private static final Map<Class<? extends Throwable>, HttpStatus> exceptionMap = new HashMap<>();

    static {
        // auth
        exceptionMap.put(AuthException.class, HttpStatus.UNAUTHORIZED);

        // validation, client error
        exceptionMap.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);

        exceptionMap.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);

        // resource errors
        exceptionMap.put(NoSuchElementException.class, HttpStatus.NOT_FOUND);

        // conflict, state error
        exceptionMap.put(IllegalStateException.class, HttpStatus.CONFLICT);

        // network error
        exceptionMap.put(HttpServerErrorException.GatewayTimeout.class, HttpStatus.GATEWAY_TIMEOUT);
        exceptionMap.put(HttpServerErrorException.ServiceUnavailable.class, HttpStatus.SERVICE_UNAVAILABLE);

        // serialization, deserialization error
        exceptionMap.put(JsonProcessingException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(HttpMessageConversionException.class, HttpStatus.BAD_REQUEST);

        // server error
        exceptionMap.put(NullPointerException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(Exception.class, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static HttpStatus getHttpStatusForException(Throwable exception) {
        return exceptionMap.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void addExceptionMapping(Class<? extends Throwable> exceptionClass, HttpStatus httpStatus) {
        exceptionMap.put(exceptionClass, httpStatus);
    }

    public static void removeExceptionMapping(Class<? extends Throwable> exceptionClass) {
        exceptionMap.remove(exceptionClass);
    }

    public static void reset() {
        exceptionMap.clear();
    }


}
