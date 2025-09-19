package de.bayer.pharmacy.edgeposapi.service;

import de.bayer.pharmacy.edgeposapi.domain.reservation.*;
import de.bayer.pharmacy.edgeposapi.domain.customer.Customer;
import de.bayer.pharmacy.edgeposapi.domain.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private final ReservationRepository repo;
    private final ProductRepository products;

    public ReservationService(ReservationRepository r, ProductRepository p) {
        this.repo = r;
        this.products = p;
    }

    @Transactional
    public Reservation reserve(Customer pat, String sku, int qty) {
        Product pr = products.findBySku(sku);
        if (pr == null) throw new IllegalStateException("product_not_found");
        Reservation r = new Reservation();
        r.setPatient(pat);
        r.setProduct(pr);
        r.setQuantity(qty);
        r.setStatus("OPEN");
        return repo.save(r);
    }

    @Transactional
    public Reservation collected(Long id) {
        Reservation r = repo.findById(id).orElseThrow(() -> new IllegalStateException("reservation_not_found"));
        r.setStatus("COLLECTED");
        return repo.save(r);
    }
}
