package de.bayer.pharmacy.edgeposapi.domain.prescription;

import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "prescription_line")
public class PrescriptionLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Prescription prescription;
    @ManyToOne(optional = false)
    private Product product;
    @Column(nullable = false)
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription p) {
        this.prescription = p;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product p) {
        this.product = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }
}
