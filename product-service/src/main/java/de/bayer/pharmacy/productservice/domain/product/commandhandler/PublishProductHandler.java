package de.bayer.pharmacy.productservice.domain.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.commands.ApproveProductCommand;
import de.bayer.pharmacy.productservice.domain.product.commands.PublishProductCommand;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PublishProductHandler implements ICommandHandler<PublishProductCommand, Product> {

    private final ProductRepository repo;
    private final ApplicationEventPublisher publisher;

    public PublishProductHandler(ProductRepository repo, ApplicationEventPublisher publisher) {
        this.repo = repo;

        this.publisher = publisher;
    }

    @Override public Class<PublishProductCommand> commandType() {
        return PublishProductCommand.class;
    }

    @Override @Transactional
    public Product handle(PublishProductCommand cmd) {

        var proOpt = repo.findBySku(cmd.sku());

        if(!proOpt.isPresent()) {
            throw new IllegalArgumentException("Sku not found");
        }

        var product = proOpt.get();

        product.publish("harry");

        product = repo.save(product);

        product.pullDomainEvents().forEach(publisher::publishEvent);

        return product;
    }
}