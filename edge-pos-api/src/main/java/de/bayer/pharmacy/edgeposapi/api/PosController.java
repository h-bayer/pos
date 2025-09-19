package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.sales.Sale;
import de.bayer.pharmacy.edgeposapi.domain.customer.*;
import de.bayer.pharmacy.edgeposapi.service.PointOfSaleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pos")
public class PosController {
    private final PointOfSaleService pos;
    private final CustomerRepository patients;

    public PosController(PointOfSaleService p, CustomerRepository pr) {
        this.pos = p;
        this.patients = pr;
    }

    @PostMapping("/scan")
    public Sale scan(@RequestParam(required = false) Long saleId, @RequestParam String sku, @RequestParam int qty) {
        return pos.scanAndAdd(saleId, sku, qty);
    }

    @PostMapping("/{saleId}/checkout")
    public Sale checkout(@PathVariable Long saleId, @RequestParam Long patientId) {
        Customer p = patients.findById(patientId).orElse(null);
        return pos.checkout(saleId, p);
    }
}
