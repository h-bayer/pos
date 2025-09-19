package de.bayer.pharmacy.edgeposapi.service;

import de.bayer.pharmacy.edgeposapi.domain.inventory.*;
import de.bayer.pharmacy.edgeposapi.domain.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final StockItemRepository stockRepo;
    private final ProductRepository productRepo;

    public InventoryService(StockItemRepository s, ProductRepository p) {
        this.stockRepo = s;
        this.productRepo = p;
    }

    private StockItem ensure(Product p) {
        StockItem s = stockRepo.findByProduct(p);
        if (s == null) {
            s = new StockItem();
            s.setProduct(p);
            s.setOnHand(0);
            s.setReserved(0);
        }
        return s;
    }

    @Transactional
    public StockItem receive(String sku, int qty) {
        Product p = productRepo.findBySku(sku);
        if (p == null) throw new IllegalStateException("product_not_found");
        StockItem s = ensure(p);
        s.setOnHand(s.getOnHand() + qty);
        return stockRepo.save(s);
    }

    @Transactional
    public StockItem dispense(String sku, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty_must_be_positive");

        Product p = productRepo.findBySku(sku);
        if (p == null) throw new IllegalStateException("product_not_found");

        // Atomically decrement if enough stock is available
        int updated = stockRepo.tryDecrement(p, qty); // returns 1 on success, 0 otherwise
        if (updated == 0) throw new IllegalStateException("insufficient_stock");

        // Reload the entity to return the current state
        StockItem s = stockRepo.findByProduct(p);
        return s;
    }
}
