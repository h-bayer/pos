package de.bayer.pharmacy.inventoryservice.application.command;

public class StoreDeliveryPositionResult {
    private String sku;
    private int remainingCount = 0;

    public boolean hasOpenAmount() {
        return remainingCount > 0;
    }

    public String getSku() {
        return sku;
    }


    public StoreDeliveryPositionResult setSku(String sku) {
        this.sku = sku;
        return this;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public StoreDeliveryPositionResult setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
        return this;
    }

    public StoreDeliveryPositionResult() {
    }

    public StoreDeliveryPositionResult(String sku, int remainingCount) {
        this.sku = sku;
        this.remainingCount = remainingCount;
    }
}
