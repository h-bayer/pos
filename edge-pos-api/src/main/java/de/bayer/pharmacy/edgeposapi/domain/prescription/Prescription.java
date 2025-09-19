package de.bayer.pharmacy.edgeposapi.domain.prescription;

import de.bayer.pharmacy.edgeposapi.domain.customer.Customer;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Customer patient;
    private String prescriber;
    private Instant issuedAt = Instant.now();
    private boolean reimbursable = true;
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionLine> lines = new ArrayList<>();

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

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String p) {
        this.prescriber = p;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant t) {
        this.issuedAt = t;
    }

    public boolean isReimbursable() {
        return reimbursable;
    }

    public void setReimbursable(boolean r) {
        this.reimbursable = r;
    }

    public List<PrescriptionLine> getLines() {
        return lines;
    }

    public void setLines(List<PrescriptionLine> l) {
        this.lines = l;
    }
}
