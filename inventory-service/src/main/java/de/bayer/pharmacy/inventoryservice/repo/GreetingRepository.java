package de.bayer.pharmacy.inventoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.inventoryservice.model.Greeting;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {}
