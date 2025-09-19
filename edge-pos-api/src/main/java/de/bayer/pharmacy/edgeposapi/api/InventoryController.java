package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.inventory.StockItem;
import de.bayer.pharmacy.edgeposapi.service.InventoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService svc;

    public InventoryController(InventoryService s) {
        this.svc = s;
    }

    @PostMapping("/receive")
    public StockItem receive(@RequestParam String sku, @RequestParam int qty) {
        return svc.receive(sku, qty);
    }

    @PostMapping("/dispense")
    public StockItem dispense(@RequestParam String sku, @RequestParam int qty) {
        return svc.dispense(sku, qty);
    }
}
