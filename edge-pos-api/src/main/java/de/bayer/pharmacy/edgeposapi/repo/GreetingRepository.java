package de.bayer.pharmacy.edgeposapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.edgeposapi.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
