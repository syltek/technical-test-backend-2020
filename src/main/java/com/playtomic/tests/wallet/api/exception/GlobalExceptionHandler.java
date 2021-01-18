package com.playtomic.tests.wallet.api.exception;

import java.util.Map;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler extends
    AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(
        final ErrorAttributes errorAttributes,
        final Resources resources,
        final ApplicationContext applicationContext,
        final ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
        final ErrorAttributes errorAttributes) {

        return RouterFunctions.route(
            RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
        final ServerRequest request) {

        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
            ErrorAttributeOptions.defaults());

        final HttpStatus status = (HttpStatus) errorPropertiesMap.get("status");

        return ServerResponse.status(status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
