package de.bayer.pharmacy.productservice.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.CommandHandler;
import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.commands.CreateProductCommand;
import de.bayer.pharmacy.productservice.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateProductHandler implements CommandHandler<CreateProductCommand, Long> {

    private final ProductRepository repo;

    public CreateProductHandler(ProductRepository repo) {
        this.repo = repo;
    }

    @Override public Class<CreateProductCommand> commandType() {
        return CreateProductCommand.class;
    }

    @Override @Transactional
    public Long handle(CreateProductCommand cmd) {
        var p = new Product(/* type */ null, null, cmd.name(), cmd.description());
        p.setSku(cmd.sku());
        repo.save(p);
        return p.getId();
    }
}