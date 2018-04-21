package com.insurance.ins.business.services;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.enums.Frequency;
import com.insurance.ins.business.models.product.AllProductsViewModel;
import com.insurance.ins.business.models.product.EditProductModel;
import com.insurance.ins.business.models.product.ProductModel;
import com.insurance.ins.business.models.product.SearchProductModel;
import com.insurance.ins.business.repositories.ProductRepository;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Primary
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final NotificationService notifyService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, NotificationService notifyService) {
        this.productRepository = productRepository;
        this.notifyService = notifyService;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        Product product = this.productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product findByIdntfr(String idntfr) {
        return productRepository.findByIdntfr(idntfr);
    }

    @Override
    public Product create(Product product) {
        return this.productRepository.saveAndFlush(product);
    }

    @Override
    public Product edit(ProductModel productModel) {
        Product product = DTOConvertUtil.convert(productModel, Product.class);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }


    @Override
    public AllProductsViewModel findAllByIdntfr(String idntfr, Pageable pageable) {

        AllProductsViewModel viewModel = new AllProductsViewModel();

        viewModel.setProducts(this.productRepository.findAllByIdntfr(idntfr, pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllProductsViewModel findAllByPage(Pageable pageable) {
        AllProductsViewModel viewModel = new AllProductsViewModel();

        viewModel.setProducts(this.productRepository.findAll(pageable));
        viewModel.setTotalPageCount(this.getTotalPages());

        return viewModel;
    }

    @Override
    public AllProductsViewModel searchProduct(SearchProductModel searchProductModel, Pageable pageable) {
        String idntfr = searchProductModel.getProductId();
        AllProductsViewModel allProductsViewModel;
        if (!idntfr.equals("")) {
            allProductsViewModel = this.findAllByIdntfr(idntfr, pageable);
        } else {
            allProductsViewModel = this.findAllByPage(pageable);
        }
        return allProductsViewModel;
    }

    @Override
    public long getTotalPages(int size) {
        return this.productRepository.count() / size;
    }

    @Override
    public boolean checkProductRules(Product product, Contract contract) {
        LocalDate startDt = contract.getStartDt();
        boolean output = true;
        if (!product.getFrequencyRule().contains(contract.getFrequency().toString())) {
            String[] freqRule = product.getFrequencyRule().split(",");
            String concat = "";
            for (String s : freqRule) {
                if (freqRule[freqRule.length - 1].equals(s)) {
                    concat += Frequency.valueOf(s).getFreqLabel() + "!";
                } else
                    concat += Frequency.valueOf(s).getFreqLabel() + ", ";
            }
            notifyService.addErrorMessage("Please fill the form correctly, allowed frequencies for this product are " + concat);
            output = false;
        }
        int age = contract.getOwner().getAge(startDt);
        int maxAge = product.getMaxAge();
        int minAge = product.getMinAge();
        if (age > maxAge) {
            notifyService.addErrorMessage(String.format("Please fill the form correctly, at contract start date Owner is %s years old, maximum allowed age for this product is %s!", age, maxAge));
            output = false;
        }
        if (age < minAge) {
            notifyService.addErrorMessage(String.format("Please fill the form correctly, at contract start date Owner is %s years old, minimum allowed age for this product is %s!", age, minAge));
            output = false;
        }
        return output;
    }

    @Override
    public void edit(Product product, @Valid EditProductModel productModel) {
        Product productFromModel = DTOConvertUtil.convert(productModel, Product.class);
        productFromModel.setIdntfr(product.getIdntfr());
        this.productRepository.saveAndFlush(productFromModel);
    }

    @Override
    public List<Product> findAllByIdntfrContains(String idntfr) {
        return this.productRepository.findAllByIdntfrContains(idntfr);
    }


    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        Assert.notNull(fieldName, "Can't be null");

        if (!fieldName.equals("idntfr")) {
            throw new UnsupportedOperationException("Field name not supported");
        }

        if (value == null) {
            return false;
        }

        return this.productRepository.existsByIdntfr(value.toString());
    }
}

