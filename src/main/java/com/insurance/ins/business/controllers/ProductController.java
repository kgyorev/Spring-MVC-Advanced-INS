package com.insurance.ins.business.controllers;

import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.models.product.AllProductsViewModel;
import com.insurance.ins.business.models.product.ProductModel;
import com.insurance.ins.business.models.product.SearchProductModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.ParseException;

@Controller
public class ProductController {
    public static final String CLIENT = "siteClient";
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DistributorService distributorService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PremiumService premiumService;
    @Autowired
    private MoneyInService moneyInService;
    @Autowired
    private NotificationService notifyService;


    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String view_all(@ModelAttribute(name = "searchProductModel") SearchProductModel searchProductModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllProductsViewModel productall = productService.searchProduct(searchProductModel, pageable);

        if ((!searchProductModel.getProductId().equals(""))
                && !productall.getProducts().hasContent()) {
            notifyService.addWarningMessage("Cannot find products with given search criteria.");
        }
        model.addAttribute("productall", productall);
        return "business/product/search-product";
    }

    @RequestMapping(value = "/products/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "productModel") ProductModel productModel, @PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }

        productModel = DTOConvertUtil.convert(product, ProductModel.class);
        model.addAttribute("productModel", productModel);
        return "business/product/edit-product";
    }

    @RequestMapping(value = "/products/confirm/edit/{id}", method = RequestMethod.GET)
    public String confirmEdit(@Valid @ModelAttribute(name = "productModel") ProductModel productModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
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
        return "business/product/confirm-edit-product";
    }

    @RequestMapping(value = "/products/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid ProductModel productModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "business/product/edit-product";
        }
        if (productModel == null) {
            notifyService.addErrorMessage("Cannot find product #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/product/edit-product";
        }

        productService.edit(productModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/products";
    }

    @RequestMapping(value = "/products/create", method = RequestMethod.GET)
    public String createPage(@ModelAttribute(name = "productModel") ProductModel productModel) {
        return "business/product/create-product";
    }


    @RequestMapping(value = "/products/confirm/create", method = RequestMethod.GET)
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


    @RequestMapping(value = "/products/create", method = RequestMethod.POST)
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
}
