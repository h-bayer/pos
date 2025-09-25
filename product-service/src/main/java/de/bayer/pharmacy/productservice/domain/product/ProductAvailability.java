package de.bayer.pharmacy.productservice.domain.product;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ProductAvailability {
    private String branchCode;

    private int availableQuantity;

    protected ProductAvailability() {

    }

    public ProductAvailability(String branchCode, int availableQuantity) {
        this.branchCode = branchCode;
        this.availableQuantity = availableQuantity;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    // equality by value
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductAvailability that)) return false;
        return availableQuantity == that.availableQuantity &&
                branchCode.equals(that.branchCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchCode, availableQuantity);
    }
}
