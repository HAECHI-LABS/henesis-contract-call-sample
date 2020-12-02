package io.haechi.henesis.contract.web;

import io.haechi.henesis.contract.domain.exception.HenesisApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ResponseBuilder responseBuilder;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult defaultException(Exception e) {
        log.error(e.getMessage(), e);
        return responseBuilder.getFailResult(-4004, "Error has occurred.");
    }

    @ExceptionHandler(HenesisApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult henesisIoException(HenesisApiException e) {
        log.error(e.getMessage(), e);
        return responseBuilder.getFailResult(-5000,
                "Something went wrong while sending request to Henesis API.");
    }
}
