package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.reservation.Reservation;
import de.bayer.pharmacy.edgeposapi.domain.customer.*;
import de.bayer.pharmacy.edgeposapi.service.ReservationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService svc;
    private final CustomerRepository patients;

    public ReservationController(ReservationService s, CustomerRepository p) {
        this.svc = s;
        this.patients = p;
    }

    @PostMapping
    public Reservation reserve(@RequestParam Long patientId, @RequestParam String sku, @RequestParam int qty) {
        Customer pat = patients.findById(patientId).orElseThrow();
        return svc.reserve(pat, sku, qty);
    }

    @PostMapping("/{id}/collected")
    public Reservation collected(@PathVariable Long id) {
        return svc.collected(id);
    }
}
