package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.AddUpdateProductRequest;
import com.nivtech.petitecaisse.domain.ProductService;
import com.nivtech.petitecaisse.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/product")
public class ProductController
{
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {this.productService = productService;}

    @GetMapping("/one")
    public @ResponseBody
    Product getProduct(@RequestParam Long id)
    {
        var product = productService.getProduct(id);
        product.setProductTransactions(product.getProductTransactions().stream().limit(5).collect(Collectors.toList()));
        return product;
    }

    @GetMapping("/all")
    public @ResponseBody
    Iterable<Product> getAllProducts()
    {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody AddUpdateProductRequest addUpdateProductRequest)
    {
        var result = productService
                .addNewProduct(addUpdateProductRequest.getName(), addUpdateProductRequest.getDescription(),
                        addUpdateProductRequest.getImageUrl(), addUpdateProductRequest.getPrice(),
                        addUpdateProductRequest.getNewQuantity());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody AddUpdateProductRequest addUpdateProductRequest)
    {
        var result = productService.updateProduct(addUpdateProductRequest.getId(), addUpdateProductRequest.getName(),
                addUpdateProductRequest.getDescription(), addUpdateProductRequest.getImageUrl(),
                addUpdateProductRequest.getPrice(), addUpdateProductRequest.getNewQuantity());

        return ResponseEntity.ok(result);
    }

}
