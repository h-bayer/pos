package de.bayer.pharmacy.edgeposapi.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    StockItem findByProduct(Product product);

    @Modifying
    @Query("update StockItem s set s.onHand = s.onHand - :qty " +
            "where s.product = :product and s.onHand >= :qty")
    int tryDecrement(Product product, int qty);
}
