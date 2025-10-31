package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.PublishProductCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductException;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductNotFoundException;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class PublishProductCommandHandler implements ICommandHandler<PublishProductCommand, Void> {

    private final ProductRepository productRepository;

    public PublishProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Void handle(PublishProductCommand command) {
        var product = productRepository.findBySku(command.getSku())
                .orElseThrow(() -> new ProductNotFoundException(" product not found:" + command.getSku()));

        if(product.canBePublished())
        {
            product.approve();

            productRepository.save(product);
        }
        else
        {
            throw new ProductException("product can not be published:" + product.getSku());
        }

        return null;
    }

    @Override
    public Class<PublishProductCommand> commandType() {
        return PublishProductCommand.class;
    }
}
