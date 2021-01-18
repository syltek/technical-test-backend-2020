package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.repository.WalletRepository;
import io.r2dbc.spi.ConnectionFactory;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class WalletApplication {

	private static final Logger log = LoggerFactory
		.getLogger(WalletApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

	@Bean
	ConnectionFactoryInitializer initializer(final ConnectionFactory connectionFactory) {

		final ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(
			new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

		log.info("Database tables created");

		return initializer;
	}


	@Bean
	public CommandLineRunner demo(final WalletRepository repository) {

		return (args) -> {

			repository.saveAll(
				Flux.just(
					Wallet.builder().balance(BigDecimal.valueOf(200))
						.currency("EUR").build(),
					Wallet.builder().balance(BigDecimal.valueOf(300))
						.currency("USD").build()))
				.subscribe().dispose();

			repository.findAll().doOnNext(wallet -> log.info("Inserted wallet {}", wallet))
				.subscribe().dispose();

		};
	}
}
