package com.wanderlei.desafioanotaai.services;

import com.wanderlei.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.wanderlei.desafioanotaai.domain.product.Product;
import com.wanderlei.desafioanotaai.domain.product.ProductDTO;
import com.wanderlei.desafioanotaai.domain.product.exceptions.ProductNotFoundException;
import com.wanderlei.desafioanotaai.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final CategoryService categoryService;
    private final ProductRepository repository;

    public ProductService(CategoryService categoryService, ProductRepository productRepository){
        this.categoryService = categoryService;
        this.repository = productRepository;
    }

    public Product insert(ProductDTO productData){
        this.categoryService.getById(productData.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        Product newProduct = new Product(productData);

        this.repository.save(newProduct);

        return newProduct;
    }

    public Product update(String id, ProductDTO productData){
        Product product = this.repository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        this.categoryService.getById(productData.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        if(!productData.title().isEmpty()) product.setTitle(productData.title());
        if(!productData.description().isEmpty()) product.setDescription(productData.description());
        if(!(productData.price() == null)) product.setPrice(productData.price());
        if(!(productData.categoryId() == null)) product.setCategory(productData.categoryId());

        this.repository.save(product);

        return product;
    }

    public void delete(String id){
        Product product = this.repository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        this.repository.delete(product);
    }

    public List<Product> getAll(){
        return this.repository.findAll();
    }
}
