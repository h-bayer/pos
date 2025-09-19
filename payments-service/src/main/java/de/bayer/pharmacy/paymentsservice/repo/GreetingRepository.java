package de.bayer.pharmacy.paymentsservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.paymentsservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
