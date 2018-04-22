package com.insurance.ins.business.controllers;

import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.models.product.AllProductsViewModel;
import com.insurance.ins.business.models.product.EditProductModel;
import com.insurance.ins.business.models.product.ProductModel;
import com.insurance.ins.business.models.product.SearchProductModel;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.annotations.Log;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;
    private final NotificationService notifyService;

    @Autowired
    public ProductController(ProductService productService, NotificationService notifyService) {
        this.productService = productService;
        this.notifyService = notifyService;
    }


    @GetMapping(value = "/products/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String view(@ModelAttribute(name = "productModel") ProductModel productModel, @PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }
        productModel = DTOConvertUtil.convert(product, ProductModel.class);
        model.addAttribute("productModel", productModel);
        return "business/product/view-product";
    }

    @GetMapping(value = "/products")
    @PreAuthorize("hasRole('MODERATOR')")
    public String view_all(@ModelAttribute(name = "searchProductModel") SearchProductModel searchProductModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllProductsViewModel productall = productService.searchProduct(searchProductModel, pageable);

        if ((!searchProductModel.getProductId().equals(""))
                && !productall.getProducts().hasContent()) {
            notifyService.addWarningMessage("Cannot find products with given search criteria.");
        }
        model.addAttribute("productall", productall);
        return "business/product/search-product";
    }

    @GetMapping(value = "/products/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editPage(@ModelAttribute(name = "productModel") EditProductModel productModel, @PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }

        productModel = DTOConvertUtil.convert(product, EditProductModel.class);
        String productIdntfr = product.getIdntfr();
        model.addAttribute("productModel", productModel);
        model.addAttribute("productIdntfr", productIdntfr);
        return "business/product/edit-product";
    }

    @GetMapping(value = "/products/confirm/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String confirmEdit(@Valid @ModelAttribute(name = "productModel") EditProductModel productModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/products";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/product/edit-product";
        }
        Product product = productService.findById(id);
        if (product == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }
        model.addAttribute("producIdntfr", product.getIdntfr());
        return "business/product/confirm-edit-product";
    }

    @PostMapping(value = "/products/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Log
    public String edit(@Valid @ModelAttribute(name = "productModel") EditProductModel productModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        Product product = productService.findById(id);
        if (product == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            model.addAttribute("productIdntfr", product.getIdntfr());
            return "business/product/edit-product";
        }

        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/product/edit-product";
        }

        productService.edit(product, productModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/products";
    }

    @GetMapping(value = "/products/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPage(@ModelAttribute(name = "productModel") ProductModel productModel) {
        return "business/product/create-product";
    }


    @GetMapping(value = "/products/confirm/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String confirmCreate(@Valid @ModelAttribute(name = "productModel") ProductModel productModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/product/create-product";
        }

        return "business/product/confirm-create-product";
    }


    @PostMapping(value = "/products/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Log
    public String create(@Valid ProductModel productModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "business/product/create-product";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/product/create-product";
        }
        Product product = DTOConvertUtil.convert(productModel, Product.class);

        productService.create(product);
        notifyService.addInfoMessage("Product with Identifier: " + product.getIdntfr() + " was created.");
        return "redirect:/";
    }

    //    REST
    @GetMapping(value = "/rest/products", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    public List<Product> allProducts(@RequestParam(value = "idntfr", required = false) String idntfr) {
        List<Product> products = this.productService.findAllByIdntfrContains(idntfr);
        return products;
    }
}
