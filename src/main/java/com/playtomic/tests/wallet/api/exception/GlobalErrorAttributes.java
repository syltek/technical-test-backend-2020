package com.playtomic.tests.wallet.api.exception;

import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final Logger log = LoggerFactory.getLogger(GlobalErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(final ServerRequest request,
        final ErrorAttributeOptions options) {
        final Map<String, Object> map = super.getErrorAttributes(
            request, options);

        final Integer status = (Integer) map.get("status");
        String message = (String) map.get("message");
        String errorToDisplay = (String) map.get("error");

        HttpStatus httpStatus =
            status != null ? HttpStatus.valueOf(status) : HttpStatus.INTERNAL_SERVER_ERROR;

        message = message != null ? message : "An unexpected error has occurred";
        errorToDisplay = errorToDisplay != null ? errorToDisplay : "Unexpected error";

        final Throwable error = getError(request);

        log.error("Error has occurred: \ntype: {}\nmessage: {}\ncause: {}", error.getClass(),
            error.getMessage(), error.getCause());
        log.debug("Error has occurred ", error);

        if (error instanceof WalletException) {
            final WalletErrorCode walletErrorCode = ((WalletException) error).getErrorCode();
            httpStatus = getHttpCodeForWalletErrorCode(walletErrorCode);
            errorToDisplay = walletErrorCode.name();
            message = error.getMessage();
        } else if (error instanceof PaymentServiceException) {
            final PaymentServiceErrorCode errorCode = ((PaymentServiceException) error)
                .getErrorCode();
            httpStatus = getHttpCodeForPaymentServiceErrorCode(errorCode);
            errorToDisplay = errorCode.name();
            message = error.getMessage();
        } else if (error instanceof ValidationException) {

            httpStatus = HttpStatus.BAD_REQUEST;
            errorToDisplay = "Invalid request parameter";
            message = error.getMessage();
        } else if (error instanceof WebExchangeBindException) {

            Map<String, String> invalidParameterMessageMap = new HashMap<>();

            final List<FieldError> fieldErrors = ((WebExchangeBindException) error)
                .getFieldErrors();
            httpStatus = HttpStatus.BAD_REQUEST;
            errorToDisplay = "Invalid request parameter";
            
            fieldErrors.forEach(fieldError -> invalidParameterMessageMap.put(fieldError.getField(),
                fieldError.getDefaultMessage()));
            message = invalidParameterMessageMap.toString();
        }
        map.put("status", httpStatus);
        map.put("message", message);
        map.put("error", errorToDisplay);

        return map;
    }

    private HttpStatus getHttpCodeForPaymentServiceErrorCode(
        final PaymentServiceErrorCode errorCode) {

        switch (errorCode) {
            case SERVICE_NOT_AVAILABLE:
            case INSUFFICIENT_AMOUNT:
                return HttpStatus.BAD_REQUEST;

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatus getHttpCodeForWalletErrorCode(final WalletErrorCode errorCode) {

        switch (errorCode) {
            case WALLET_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case INSUFFICIENT_BALANCE:
            case CURRENCY_NOT_SUPPORTED:
                return HttpStatus.BAD_REQUEST;

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}