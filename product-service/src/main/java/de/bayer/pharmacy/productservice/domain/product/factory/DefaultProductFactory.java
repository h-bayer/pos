package de.bayer.pharmacy.productservice.domain.product.factory;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class DefaultProductFactory implements IProductFactory {

    private final Map<ProductType, IProductCreator> creators;

    DefaultProductFactory(Set<IProductCreator> creators) {
        this.creators = creators.stream().collect(Collectors.toMap(IProductCreator::type, c -> c));
    }

    public Product create(String sku, String name, String desc, ProductType type) {
        var creator = Optional.ofNullable(creators.get(type))
                .orElseThrow(() -> new IllegalArgumentException("No creator for " + type));

        return creator.create(sku, name, desc);
    }
}