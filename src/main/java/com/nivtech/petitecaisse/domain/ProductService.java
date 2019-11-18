package com.nivtech.petitecaisse.domain;

import com.nivtech.petitecaisse.domain.entity.Product;
import com.nivtech.petitecaisse.domain.entity.Stock;
import com.nivtech.petitecaisse.exception.BadRequestException;
import com.nivtech.petitecaisse.exception.ResourceNotFoundException;
import com.nivtech.petitecaisse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService
{

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {this.productRepository = productRepository;}

    public Product getProduct(Long id)
    {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product", "id", id));
    }

    public List<Product> getAllProducts()
    {
        var products = productRepository.findAll();
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addNewProduct(String name, String description, String imageUrl, Double price, Integer quantity)
    {
        if (name == null || name.isEmpty())
        {
            throw new BadRequestException("Le nom ne doit pas être vide");
        }

        var product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        if (quantity != null)
        {
            var stock = Optional.ofNullable(product.getStock()).orElseGet(Stock::new);
            stock.setQuantity(quantity);
            product.setStock(stock);
        }

        return productRepository.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product updateProduct(Long id, String name, String description, String imageUrl, Double price,
            Integer newQuantity)
    {
        var product = getProduct(id);
        if (name != null) { product.setName(name); }
        if (description != null) { product.setDescription(description); }
        if (price != null) { product.setPrice(price); }
        if (imageUrl != null) { product.setImageUrl(imageUrl); }

        if (newQuantity != null)
        {
            var stock = Optional.ofNullable(product.getStock()).orElseGet(Stock::new);
            stock.setQuantity(newQuantity);
            product.setStock(stock);
        }

        return productRepository.save(product);
    }
}
