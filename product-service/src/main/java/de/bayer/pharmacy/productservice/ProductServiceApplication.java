package de.bayer.pharmacy.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"de.bayer.pharmacy.common.outbox",       // OutboxRepository
		"de.bayer.pharmacy.productservice"       // any repos in the app
})
@EntityScan(basePackages = {
		"de.bayer.pharmacy.common",              // OutboxMessage entity lives here
		"de.bayer.pharmacy.productservice"       // your app entities
})
@ComponentScan(basePackages = {
		"de.bayer.pharmacy.common",   // the common module
		"de.bayer.pharmacy.productservice"   // your app/domain
})
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
