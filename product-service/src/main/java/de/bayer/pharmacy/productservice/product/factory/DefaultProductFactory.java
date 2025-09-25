package de.bayer.pharmacy.productservice.product.factory;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class DefaultProductFactory implements IProductFactory {

    private final Map<ProductType, ProductCreator> creators;

    DefaultProductFactory(Set<ProductCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(ProductCreator::type, c -> c));
    }

    public Product create(long sku, String name, String desc, ProductType type, Map<String,Integer> avail) {
        var creator = Optional.ofNullable(creators.get(type))
                .orElseThrow(() -> new IllegalArgumentException("No creator for " + type));
        return creator.create(sku, name, desc, avail);
    }
}