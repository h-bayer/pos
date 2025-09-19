package de.bayer.pharmacy.labellocalizationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.labellocalizationservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
