package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.models.product.AllProductsViewModel;
import com.insurance.ins.business.models.product.EditProductModel;
import com.insurance.ins.business.models.product.ProductModel;
import com.insurance.ins.business.models.product.SearchProductModel;
import com.insurance.ins.utils.interfaces.FieldValueExists;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface ProductService extends FieldValueExists {
    List<Product> findAll();
    Product findById(Long id);
    Product findByIdntfr(String idntfr);
    Product create(Product product);
    Product edit(ProductModel product);
    void deleteById(Long id);

    AllProductsViewModel findAllByIdntfr(String idntfr, Pageable pageable);

    AllProductsViewModel findAllByPage(Pageable pageable);
    AllProductsViewModel searchProduct(SearchProductModel searchProductModel, Pageable pageable);
    default long getTotalPages() {
        return getTotalPages(10);
    }
    long getTotalPages(int size);


    boolean checkProductRules(Product product, Contract contract);

    void edit(Product product, @Valid EditProductModel productModel);

}
