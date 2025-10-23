package de.bayer.pharmacy.inventoryservice.application.command;

public class StoreDeliveryPositionResult {
    private long sku;
    private int openAmount = 0;

    public boolean hasOpenAmount() {
        return openAmount > 0;
    }

    public long getSku() {
        return sku;
    }

    public void setSku(long sku) {
        this.sku = sku;
    }

    public int getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(int openAmount) {
        this.openAmount = openAmount;
    }
}
