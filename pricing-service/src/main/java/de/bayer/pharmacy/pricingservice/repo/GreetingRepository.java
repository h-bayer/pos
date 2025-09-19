package de.bayer.pharmacy.pricingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.pricingservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
