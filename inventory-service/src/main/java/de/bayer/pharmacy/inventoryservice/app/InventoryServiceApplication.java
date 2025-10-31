package de.bayer.pharmacy.inventoryservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = "de.bayer.pharmacy"   // <— scan both service + common
)
@EnableJpaRepositories(basePackages = "de.bayer.pharmacy")  // <— find repos in common
@EntityScan(basePackages = "de.bayer.pharmacy")              // <— find entities in common
public class InventoryServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(InventoryServiceApplication.class, args);
  }
}


//TODO: Implement productcreated kafka eventhandler set product status to DRAFT or so