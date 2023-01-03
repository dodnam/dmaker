package com.fast.program.dmaker.exception;

import com.fast.program.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.fast.program.dmaker.exception.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.fast.program.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e,
                                               HttpServletRequest request) {
        log.error("errorCode : {}, url : {}, message : {}", e.getDMakerErrorCode(),
                request.getRequestURI(), e.getDetailMessage());
        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class, // Post인데 Get으로 호출하는 등의 문제 발생 시
            MethodArgumentNotValidException.class // NotNull, Min, Max validation에서 문제 발생 시
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class) // 기타 등등 에러
    public DMakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

}
