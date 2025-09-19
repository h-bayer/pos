package de.bayer.pharmacy.insurerintegrationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.insurerintegrationservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
