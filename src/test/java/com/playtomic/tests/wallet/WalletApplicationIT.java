package com.playtomic.tests.wallet;


import com.playtomic.tests.wallet.api.model.WalletChargeDetails;
import com.playtomic.tests.wallet.api.model.WalletTopUpDetails;
import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletErrorCode;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = WalletApplication.class)
@ActiveProfiles(profiles = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WalletApplicationIT {

    @Autowired
    private ApplicationContext context;

    @Test
    void getInfo_walletNotFound_http404notFound() {

        final long walletId = 30L;

        WebTestClient.bindToApplicationContext(context)
            .build()
            .get()
            .uri("/wallet/{walletId}", walletId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    @Order(-1)
    void getInfo_walletExists_http200success() {

        final long walletId = 1L;

        WebTestClient.bindToApplicationContext(context)
            .build()
            .get()
            .uri("/wallet/{walletId}", walletId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.balance").isEqualTo(200)
            .jsonPath("$.currency").isEqualTo("EUR");
    }

    @Test
    void charge_invalidParameters_http400BadRequest() {

        final long walletId = 1L;

        final WalletChargeDetails chargeDetails = WalletChargeDetails.builder()
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/charge", walletId)
            .body(Mono.just(chargeDetails), WalletChargeDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo("Invalid request parameter")
            .jsonPath("$.message")
            .value(o -> {
                final String message = (String) o;
                Assertions.assertTrue(message.contains("amount=must not be null"));
                Assertions.assertTrue(message.contains("currency=must not be blank"));
                ;
            });
    }

    @Test
    void charge_insufficientBalance_http400BadRequest() {

        final long walletId = 1L;

        final WalletChargeDetails chargeDetails = WalletChargeDetails.builder()
            .amount(BigDecimal.valueOf(300))
            .currency("EUR")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/charge", walletId)
            .body(Mono.just(chargeDetails), WalletChargeDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo(WalletErrorCode.INSUFFICIENT_BALANCE.name())
            .jsonPath("$.message").isEqualTo(WalletErrorCode.INSUFFICIENT_BALANCE.getDescription());
    }

    @Test
    void charge_incompatibleCurrency_http400BadRequest() {

        final long walletId = 1L;

        final WalletChargeDetails chargeDetails = WalletChargeDetails.builder()
            .amount(BigDecimal.valueOf(100))
            .currency("USD")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/charge", walletId)
            .body(Mono.just(chargeDetails), WalletChargeDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo(WalletErrorCode.CURRENCY_NOT_SUPPORTED.name())
            .jsonPath("$.message")
            .isEqualTo(
                WalletErrorCode.CURRENCY_NOT_SUPPORTED.getDescription() + ": This wallet uses EUR");
    }


    @Test
    void charge_walletNotFound_http404notFound() {

        final long walletId = 30L;

        final WalletChargeDetails chargeDetails = WalletChargeDetails.builder()
            .amount(BigDecimal.valueOf(100))
            .currency("USD")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/charge", walletId)
            .body(Mono.just(chargeDetails), WalletChargeDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void charge_amountAndCurrencyOk_http200success() {

        final long walletId = 1L;

        final WalletChargeDetails chargeDetails = WalletChargeDetails.builder()
            .amount(BigDecimal.valueOf(100))
            .currency("EUR")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/charge", walletId)
            .body(Mono.just(chargeDetails), WalletChargeDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.balance").isEqualTo(100)
            .jsonPath("$.currency").isEqualTo("EUR");

    }


    @Test
    void topUp_invalidParameters_http400BadRequest() {

        final long walletId = 1L;

        final WalletTopUpDetails topUpDetails = WalletTopUpDetails.builder()
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/top-up", walletId)
            .body(Mono.just(topUpDetails), WalletTopUpDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo("Invalid request parameter")
            .jsonPath("$.message")
            .value(o -> {
                final String message = (String) o;
                Assertions.assertTrue(message.contains("amount=must not be null"));
                Assertions.assertTrue(message.contains("currency=must not be blank"));
                Assertions.assertTrue(message.contains("paymentServiceName=must not be blank"));
            });
    }

    @Test
    void topUp_paymentServiceNotAvailable_http400BadRequest() {

        final long walletId = 1L;

        final WalletTopUpDetails topUpDetails = WalletTopUpDetails.builder()
            .paymentServiceName("notAvailable")
            .amount(BigDecimal.valueOf(100))
            .currency("EUR")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/top-up", walletId)
            .body(Mono.just(topUpDetails), WalletTopUpDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo(PaymentServiceErrorCode.SERVICE_NOT_AVAILABLE.name())
            .jsonPath("$.message")
            .isEqualTo(PaymentServiceErrorCode.SERVICE_NOT_AVAILABLE.getDescription());
    }

    @Test
    void topUp_paymentChargeError_http400BadRequest() {
        final long walletId = 1L;

        final WalletTopUpDetails topUpDetails = WalletTopUpDetails.builder()
            .paymentServiceName("thirdPartyPaymentService")
            .amount(BigDecimal.valueOf(1))
            .currency("EUR")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/top-up", walletId)
            .body(Mono.just(topUpDetails), WalletTopUpDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo(PaymentServiceErrorCode.INSUFFICIENT_AMOUNT.name())
            .jsonPath("$.message")
            .isEqualTo(
                PaymentServiceErrorCode.INSUFFICIENT_AMOUNT.getDescription()
                    + ": The minimum amount to charge in thirdPartyPaymentService service is 10");
    }

    @Test
    void topUp_incompatibleCurrency_http400BadRequest() {
        final long walletId = 1L;

        final WalletTopUpDetails topUpDetails = WalletTopUpDetails.builder()
            .paymentServiceName("thirdPartyPaymentService")
            .amount(BigDecimal.valueOf(1000))
            .currency("USD")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/top-up", walletId)
            .body(Mono.just(topUpDetails), WalletTopUpDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
            .jsonPath("$.error").isEqualTo(WalletErrorCode.CURRENCY_NOT_SUPPORTED.name())
            .jsonPath("$.message")
            .isEqualTo(
                WalletErrorCode.CURRENCY_NOT_SUPPORTED.getDescription() + ": This wallet uses EUR");
    }

    @Test
    void topUp_amountCurrencyAndPaymentServiceOk_success() {

        final long walletId = 2L;

        final WalletTopUpDetails topUpDetails = WalletTopUpDetails.builder()
            .paymentServiceName("thirdPartyPaymentService")
            .amount(BigDecimal.valueOf(1000))
            .currency("USD")
            .build();

        WebTestClient.bindToApplicationContext(context)
            .build()
            .post()
            .uri("/wallet/{walletId}/top-up", walletId)
            .body(Mono.just(topUpDetails), WalletTopUpDetails.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(2)
            .jsonPath("$.balance").isEqualTo(1300)
            .jsonPath("$.currency").isEqualTo("USD");
    }

}
