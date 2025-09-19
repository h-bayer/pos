package de.bayer.pharmacy.edgeposapi.service;

import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import de.bayer.pharmacy.edgeposapi.domain.product.ProductRepository;
import de.bayer.pharmacy.edgeposapi.domain.sales.*;
import de.bayer.pharmacy.edgeposapi.domain.customer.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PointOfSaleService {
    private final ProductRepository productRepo;
    private final SaleRepository saleRepo;

    public PointOfSaleService(ProductRepository p, SaleRepository s) {
        this.productRepo = p;
        this.saleRepo = s;
    }

    @Transactional
    public Sale scanAndAdd(Long saleId, String sku, int qty) {
        Sale sale = (saleId == null) ? new Sale() : saleRepo.findById(saleId).orElse(new Sale());
        Product p = productRepo.findBySku(sku);
        if (p == null) throw new IllegalStateException("product_not_found");
        SaleLine line = new SaleLine();
        line.setSale(sale);
        line.setProduct(p);
        line.setQuantity(qty);
        line.setUnitPrice(p.getUnitPrice());
        line.setLineTotal(p.getUnitPrice().multiply(BigDecimal.valueOf(qty)));
        sale.getLines().add(line);
        sale.setTotalNet(sale.getLines().stream().map(SaleLine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        sale.setTotalGross(sale.getTotalNet());
        return saleRepo.save(sale);
    }

    @Transactional
    public Sale checkout(Long saleId, Customer patient) {
        Sale sale = saleRepo.findById(saleId).orElseThrow(() -> new IllegalStateException("sale_not_found"));
        sale.setPatient(patient);
        sale.setPaymentStatus("CAPTURED");
        return saleRepo.save(sale);
    }
}
