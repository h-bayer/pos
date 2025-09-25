package de.bayer.pharmacy.productservice.domain.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.commands.DeleteProductCommand;
import de.bayer.pharmacy.productservice.domain.product.factory.IProductFactory;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteProductHandler implements ICommandHandler<DeleteProductCommand, Product> {

    private final ProductRepository repo;
    private final IProductFactory productFactory;

    public DeleteProductHandler(ProductRepository repo, IProductFactory productFactory) {
        this.repo = repo;
        this.productFactory = productFactory;
    }

    @Override public Class<DeleteProductCommand> commandType() {
        return DeleteProductCommand.class;
    }

    @Override @Transactional
    public Product handle(DeleteProductCommand cmd) {
        throw new NotImplementedException();




    }
}