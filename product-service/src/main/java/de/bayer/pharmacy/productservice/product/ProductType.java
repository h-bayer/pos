package de.bayer.pharmacy.productservice.product;

public enum ProductType {
    OTC(false, false),
    NARCOTIC(true, true),
    RX(true, false),
    COMMODITY(false, false);

    private final boolean requiresPrescription;
    private final boolean controlledSubstance;

    ProductType(boolean requiresPrescription, boolean controlledSubstance) {
        this.requiresPrescription = requiresPrescription;
        this.controlledSubstance = controlledSubstance;
    }

    public boolean requiresPrescription() {
        return requiresPrescription;
    }

    public boolean isControlledSubstance() {
        return controlledSubstance;
    }
}