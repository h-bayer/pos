package de.bayer.pharmacy.productservice.domain.product.commandhandler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.commands.GetProductCommand;
import de.bayer.pharmacy.productservice.domain.product.exceptions.ProductException;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetProductCommandHandler implements ICommandHandler<GetProductCommand, Product> {

    private final ProductRepository productRepository;

    public GetProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product handle(GetProductCommand command) {
        var product = this.productRepository.findBySku(command.sku()).orElseThrow(()->new ProductException("Product not found" + command.sku()));
        return product;
    }

    @Override
    public Class<GetProductCommand> commandType() {
        return GetProductCommand.class;
    }
}
