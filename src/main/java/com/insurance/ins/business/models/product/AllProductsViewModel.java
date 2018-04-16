package com.insurance.ins.business.models.product;

import com.insurance.ins.business.entites.Product;
import org.springframework.data.domain.Page;

public class AllProductsViewModel {
    private Page<Product> products;

    private long totalPageCount;

    public Page<Product> getProducts() {
        return products;
    }

    public void setProducts(Page<Product> products) {
        this.products = products;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
