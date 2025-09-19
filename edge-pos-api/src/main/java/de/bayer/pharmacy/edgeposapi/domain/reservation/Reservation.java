package de.bayer.pharmacy.edgeposapi.domain.reservation;

import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import de.bayer.pharmacy.edgeposapi.domain.customer.Customer;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Customer patient;
    @ManyToOne(optional = false)
    private Product product;
    @Column(nullable = false)
    private int quantity;
    private Instant reservedAt = Instant.now();
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getPatient() {
        return patient;
    }

    public void setPatient(Customer p) {
        this.patient = p;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product pr) {
        this.product = pr;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant t) {
        this.reservedAt = t;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String s) {
        this.status = s;
    }
}
