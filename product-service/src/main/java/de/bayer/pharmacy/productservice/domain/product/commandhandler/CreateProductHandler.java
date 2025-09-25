package de.bayer.pharmacy.productservice.domain.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.commands.CreateProductCommand;
import de.bayer.pharmacy.productservice.domain.product.factory.IProductFactory;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateProductHandler implements ICommandHandler<CreateProductCommand, Product> {

    private final ProductRepository repo;
    private final IProductFactory productFactory;

    public CreateProductHandler(ProductRepository repo, IProductFactory productFactory) {
        this.repo = repo;
        this.productFactory = productFactory;
    }

    @Override public Class<CreateProductCommand> commandType() {
        return CreateProductCommand.class;
    }

    @Override @Transactional
    public Product handle(CreateProductCommand cmd) {
        var product = productFactory.create(
                cmd.sku(), cmd.name(), cmd.description(), cmd.type()
        );


        // repo expects the concrete entity type; no interface possible
        var result = repo.save(product);
        return result;
    }
}