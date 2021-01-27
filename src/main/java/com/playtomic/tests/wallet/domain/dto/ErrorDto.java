package com.playtomic.tests.wallet.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.ex.ErrorException;
import com.playtomic.tests.wallet.ex.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    @JsonProperty(value = "code")
    private String code;
    @JsonProperty(value = "message")
    private String message;

    public static ErrorDto fromErr(ErrorException ex) {
        ErrorType errorType = ex.getErrorType();
        String errorCode = errorType.getCode();
        String errorMessage = ex.getMessage();
        return new ErrorDto(errorCode, errorMessage);
    }

    public static ErrorDto fromEx(Exception ex) {
        String errorMessage = ex.getLocalizedMessage();
        return new ErrorDto(null, errorMessage);
    }
}