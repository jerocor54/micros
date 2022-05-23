package com.service.product.service;

import com.service.product.entity.Category;
import com.service.product.entity.Product;
import com.service.product.respository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl  implements  ProductService{

    private final ProductRespository productRespository;

    @Override
    public List<Product> getProducts() {
        return this.productRespository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return this.productRespository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreatedAt(new Date());
        return this.productRespository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productDB  = getProduct(product.getId());
        if(productDB  == null){
            return null;
        }
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setCategory(product.getCategory());
        productDB.setPrice(product.getPrice());
        return this.productRespository.save(productDB);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product productDB  = getProduct(id);
        if(productDB  == null){
            return null;
        }
        productDB.setStatus("DELETED");
        return this.productRespository.save(productDB);
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return this.productRespository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product productDB  = getProduct(id);
        if(productDB  == null){
            return null;
        }
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return this.productRespository.save(productDB);
    }
}
