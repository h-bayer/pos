package de.bayer.pharmacy.edgeposapi.domain.inventory;

import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "stock_item")
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Product product;
    @Column(nullable = false)
    private int onHand = 0;
    @Column(nullable = false)
    private int reserved = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product p) {
        this.product = p;
    }

    public int getOnHand() {
        return onHand;
    }

    public void setOnHand(int v) {
        this.onHand = v;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int v) {
        this.reserved = v;
    }
}
