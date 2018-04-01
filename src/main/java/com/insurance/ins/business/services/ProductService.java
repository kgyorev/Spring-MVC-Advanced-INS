package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product findByIdntfr(String idntfr);
    Product create(Product product);
    Product edit(Product product);
    void deleteById(Long id);
}
