package de.bayer.pharmacy.productservice.domain.product;

import jakarta.persistence.*;

@Embeddable
public class ProductImage {


    private String imageUrl;

    //banner, main product area, ...
    private String targetAreaName;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetAreaName() {
        return targetAreaName;
    }

    public void setTargetAreaName(String targetAreaName) {
        this.targetAreaName = targetAreaName;
    }

    protected ProductImage() {
    }



    public ProductImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

