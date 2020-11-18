package com.playtomic.tests.wallet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.net.URI;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.playtomic.tests.wallet.dto.ErrorResponse;
import com.playtomic.tests.wallet.dto.Payment;
import com.playtomic.tests.wallet.dto.Wallet;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { WalletApplication.class })
@ActiveProfiles({ "test" })
@EnableAutoConfiguration
public class WalletFunctionalTests {
		
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void getWalletById() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		String useCase = "/v1/wallet/fe4b59f1-b790-4603-8e5b-148a2254efab";
		ResponseEntity<Wallet> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.GET, entity, Wallet.class);

		assertEquals(HttpStatus.OK.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.OK.value()) {
			Wallet object = exchange.getBody();
			assertNotNull(object);
			assertNotNull(object.getId());
			assertNotNull(object.getBalance());
			
			assertEquals(object.getId(), "fe4b59f1-b790-4603-8e5b-148a2254efab");
			assertThat(object.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(100.00)));
		}
	}
	
	@Test
	public void getWalletByIdKO() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		String useCase = "/v1/wallet/hjfgjfgj";
		ResponseEntity<ErrorResponse> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.GET, entity, ErrorResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getErrorMessage(), "The wallet requested does not exist.");
		}
	}
	
	
	@Test
	public void makePayment() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fe4b59f1-b790-4603-8e5b-148a2254efac", new BigDecimal(20.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/make-payment";
		
		ResponseEntity<Wallet> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, Wallet.class);
		
		assertEquals(HttpStatus.OK.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.OK.value()) {
			Wallet object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getId(), "fe4b59f1-b790-4603-8e5b-148a2254efac");
			assertThat(object.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(80.00)));
		}
	}
	
	@Test
	public void makePaymentKOBalance() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fe4b59f1-b790-4603-8e5b-148a2254efac", new BigDecimal(20000.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/make-payment";
		
		ResponseEntity<ErrorResponse> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, ErrorResponse.class);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getErrorMessage(), "Can not make payment, balance is not enough.");
		}
	}
	
	@Test
	public void makePaymentKONotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fgfdgfd", new BigDecimal(20000.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/make-payment";
		
		ResponseEntity<ErrorResponse> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, ErrorResponse.class);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getErrorMessage(), "The wallet requested does not exist.");
		}
	}

	@Test
	public void rechargeWallet() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fe4b59f1-b790-4603-8e5b-148a2254efad", new BigDecimal(20.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/recharge-wallet";
		
		ResponseEntity<Wallet> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, Wallet.class);
		
		assertEquals(HttpStatus.OK.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.OK.value()) {
			Wallet object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getId(), "fe4b59f1-b790-4603-8e5b-148a2254efad");
			assertThat(object.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(120.00)));
		}
	}
	
	@Test
	public void rechargeWalletKOAmount() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fe4b59f1-b790-4603-8e5b-148a2254efad", new BigDecimal(2.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/recharge-wallet";
		
		ResponseEntity<ErrorResponse> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, ErrorResponse.class);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getErrorMessage(), "Cannot process payment of less than 5€.");
		}
	}
	
	@Test
	public void rechargeWalletKONotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json; charset=utf-8");

		Payment body = new Payment("fgfdgfd", new BigDecimal(20000.00));
		
		HttpEntity<Payment> entity = new HttpEntity<Payment>(body, headers);
		String useCase = "/v1/recharge-wallet";
		
		ResponseEntity<ErrorResponse> exchange = this.testRestTemplate.exchange(URI.create(useCase), HttpMethod.POST, entity, ErrorResponse.class);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getStatusCodeValue());
		Assert.assertNotNull(exchange.getBody());
		if (exchange.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse object = exchange.getBody();
			assertNotNull(object);
			assertEquals(object.getErrorMessage(), "The wallet requested does not exist.");
		}
	}

}
