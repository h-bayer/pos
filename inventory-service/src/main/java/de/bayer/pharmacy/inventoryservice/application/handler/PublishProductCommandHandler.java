package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.PublishProductCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductException;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductNotFoundException;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PublishProductCommandHandler implements ICommandHandler<PublishProductCommand, Void> {

    private static final Logger log = LoggerFactory.getLogger(PublishProductCommandHandler.class);
    private final ProductRepository productRepository;

    public PublishProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    /// A new product has been published. Create it here as well.
    @Override
    public Void handle(PublishProductCommand command) {

        if(!productRepository.existsBySku(command.getSku())) {
            var newProduct = new Product()
                    .setSku(command.getSku())
                    .setName(command.getName());

            productRepository.save(newProduct);
        }
        else
        {
            log.info("Product already exists with SKU {}", command.getSku());
        }


        return null;
    }

    @Override
    public Class<PublishProductCommand> commandType() {
        return PublishProductCommand.class;
    }
}
