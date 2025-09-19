package de.bayer.pharmacy.prescriptionservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.prescriptionservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
