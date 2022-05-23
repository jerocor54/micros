package com.service.product.service;

import com.service.product.entity.Category;
import com.service.product.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> getProducts();

    public Product getProduct(Long id);

    public Product createProduct(Product product);

    public Product updateProduct(Product product);

    public Product deleteProduct(Long id);

    public List<Product> getProductsByCategory(Category category);

    public  Product updateStock(Long id, Double quantity);

}
