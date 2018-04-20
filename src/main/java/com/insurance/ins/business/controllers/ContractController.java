package com.insurance.ins.business.controllers;

import com.insurance.ins.business.batch.BusinessBatch;
import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.contract.ContractModel;
import com.insurance.ins.business.models.contract.EditContractModel;
import com.insurance.ins.business.models.contract.SearchContractModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.models.AllMoneyInsViewModel;
import com.insurance.ins.financial.models.AllPremiumsViewModel;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.utils.DTOConvertUtil;
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

@Controller
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProductService productService;
//    @Autowired
//    private DistributorService distributorService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PremiumService premiumService;
    @Autowired
    private MoneyInService moneyInService;
    @Autowired
    private NotificationService notifyService;


    @RequestMapping(value = "/batch", method = RequestMethod.GET)
    public String batch() {

        BusinessBatch batch = new BusinessBatch(this.premiumService);
        batch.scheduleTaskUsingCronExpression();

        return "redirect:/";
    }

    @RequestMapping(value = "/premiumBillingBatch", method = RequestMethod.GET)
    public String batchPremium() {

        BusinessBatch batch = new BusinessBatch(this.premiumService);
        batch.premiumBillingBatch();
        return "redirect:/";
    }

    @RequestMapping(value = "/contracts/{id}", method = RequestMethod.GET)
    public String view(@ModelAttribute(name = "contractModel") ContractModel contractModel, @PathVariable("id") Long id, Model model, @PageableDefault(size = 10) Pageable page) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        String selectedTab = contractModel.getSelectedTab();
        contractModel = DTOConvertUtil.convert(contract, ContractModel.class);
        contractModel.setSelectedTab(selectedTab);
        AllPremiumsViewModel premiumAll = premiumService.searchPremiumForContract(contract, page);
        model.addAttribute("premiumAll", premiumAll);
        AllMoneyInsViewModel moneyInAll = moneyInService.searchMoneyInsForContract(contract, page);
        model.addAttribute("moneyInAll", moneyInAll);
        model.addAttribute("contractModel", contractModel);
        return "business/contract/view-contract";
    }

    @RequestMapping(value = "/contracts", method = RequestMethod.GET)
    public String view_all(@ModelAttribute(name = "searchContractModel") SearchContractModel searchContractModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllContractsViewModel contractall = contractService.searchContract(searchContractModel, pageable);

        if (
                (!searchContractModel.getCntrctId().equals("") || !(searchContractModel.getStatus() == null))
                        && !contractall.getContracts().hasContent()) {
            notifyService.addWarningMessage("Cannot find contracts with given search criteria.");
        }
        model.addAttribute("contractall", contractall);
        return "business/contract/search-contract";
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "contractModel") EditContractModel contractModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        contractModel = DTOConvertUtil.convert(contract, EditContractModel.class);
        model.addAttribute("contractModel", contractModel);
        model.addAttribute("contractProductId", contract.getProduct().getIdntfr());
        model.addAttribute("contractOwnerId", contract.getOwner().getId());
        return "business/contract/edit-contract";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/confirm/edit/{id}", method = RequestMethod.GET)
    public String confirmEdit(@Valid @ModelAttribute(name = "contractModel") EditContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            return "redirect:/contracts";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            model.addAttribute("contractProductId", contract.getProduct().getIdntfr());
            model.addAttribute("contractOwnerId", contract.getOwner().getId());
            return "business/contract/edit-contract";
        }
        Contract contractEdit = contractService.prepareContractForEdit(contract,contractModel);
        Double premiumAmount = contractService.calculatePremiumAmount(contractEdit);
        contractModel.setPremiumAmount(premiumAmount);
        model.addAttribute("contractProductId", contract.getProduct().getIdntfr());
        model.addAttribute("contractOwnerId", contract.getOwner().getId());
        return "business/contract/confirm-edit-contract";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(name = "contractModel") EditContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        model.addAttribute("contractProductId", contract.getProduct().getIdntfr());
        model.addAttribute("contractOwnerId", contract.getOwner().getId());
        if (action.equals("return")) {
            return "business/contract/edit-contract";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/contract/edit-contract";
        }
        contractService.edit(contract, contractModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/contracts";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/create", method = RequestMethod.GET)
    public String createPage(@ModelAttribute(name = "contractModel") ContractModel contractMode) {
        int a = 1/0;
        return "business/contract/create-contract";
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/confirm/create", method = RequestMethod.GET)
    public String confirmCreate(@Valid @ModelAttribute(name = "contractModel") ContractModel contractModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/contract/create-contract";
        }
        Contract contract = contractService.prepareContractForCreation(contractModel);
        if (!productService.checkProductRules(contract.getProduct(), contract)) {
            return "business/contract/create-contract";
        }

        Double premiumAmount = contractService.calculatePremiumAmount(contract);
        contractModel.setPremiumAmount(premiumAmount);
        return "business/contract/confirm-create-contract";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @RequestMapping(value = "/contracts/create", method = RequestMethod.POST)
    public String create(@Valid ContractModel contractModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "business/contract/create-contract";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/contract/create-contract";
        }
        Contract contract = contractService.prepareContractForCreation(contractModel);
        if (!productService.checkProductRules(contract.getProduct(), contract)) {
            return "business/contract/create-contract";
        }
        contractService.create(contract);
        notifyService.addInfoMessage("Contract with Id: " + contract.getId() + " was created.");
        return "redirect:/";
    }
}
