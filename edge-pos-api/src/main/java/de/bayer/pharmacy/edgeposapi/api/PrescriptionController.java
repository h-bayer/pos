package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.prescription.*;
import de.bayer.pharmacy.edgeposapi.domain.product.*;
import de.bayer.pharmacy.edgeposapi.domain.customer.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionRepository prescriptionRepository;
    private final CustomerRepository patientRepository;
    private final ProductRepository productRepository;

    public PrescriptionController(PrescriptionRepository r, CustomerRepository p, ProductRepository pr) {
        this.prescriptionRepository = r;
        this.patientRepository = p;
        this.productRepository = pr;
    }

    @PostMapping
    @Transactional
    public Prescription create(@RequestParam Long patientId, @RequestParam String prescriber) {
        Customer pat = patientRepository.findById(patientId).orElseThrow();
        Prescription pr = new Prescription();
        pr.setPatient(pat);
        pr.setPrescriber(prescriber);
        return prescriptionRepository.save(pr);
    }

    @PostMapping("/{id}/add")
    @Transactional
    public Prescription add(@PathVariable Long id, @RequestParam String sku, @RequestParam int qty) {
        Prescription pr = prescriptionRepository.findById(id).orElseThrow();
        Product p = productRepository.findBySku(sku);
        if (p == null) throw new IllegalStateException("product_not_found");
        PrescriptionLine line = new PrescriptionLine();
        line.setPrescription(pr);
        line.setProduct(p);
        line.setQuantity(qty);
        pr.getLines().add(line);
        return prescriptionRepository.save(pr);
    }

    @GetMapping("/{id}")
    public Prescription one(@PathVariable Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }
}
