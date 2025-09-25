package de.bayer.pharmacy.productservice.domain.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.commands.DeleteProductCommand;
import de.bayer.pharmacy.productservice.domain.product.factory.IProductFactory;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivilegedActionException;

@Component
public class DeleteProductHandler implements ICommandHandler<DeleteProductCommand, Void> {

    private final ProductRepository repo;
    private final IProductFactory productFactory;
    private final ApplicationEventPublisher publisher;

    public DeleteProductHandler(ProductRepository repo, IProductFactory productFactory, ApplicationEventPublisher publisher) {
        this.repo = repo;
        this.productFactory = productFactory;
        this.publisher = publisher;
    }

    @Override public Class<DeleteProductCommand> commandType() {
        return DeleteProductCommand.class;
    }

    @Override
    @Transactional
    public Void handle(DeleteProductCommand cmd) {

        var prodOpt = this.repo.findBySku(cmd.sku());

        if (prodOpt.isEmpty()) {
            throw new IllegalArgumentException("Product sku " + cmd.sku() + " not found");
        }

        var product = prodOpt.get();

        if(!product.canBeDeleted()) {
            throw new IllegalArgumentException("Product sku " + cmd.sku() + " cannot be deleted");
        }

        product.prepareDeletion();

        this.repo.delete(prodOpt.get());

        product.pullDomainEvents().forEach(publisher::publishEvent);

        return null;
    }
}