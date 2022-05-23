package com.service.product.respository;

import com.service.product.entity.Category;
import com.service.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRespository  extends JpaRepository<Product, Long>  {
    public List<Product> findByCategory(Category category);
}
