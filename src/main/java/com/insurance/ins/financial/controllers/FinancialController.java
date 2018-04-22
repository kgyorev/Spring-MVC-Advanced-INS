package com.insurance.ins.financial.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.models.MoneyInModel;
import com.insurance.ins.financial.models.PremiumModel;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.annotations.Log;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.insurance.ins.technical.store.WebConstants.*;

@Controller
public class FinancialController {

    private final ContractService contractService;
    private final PremiumService premiumService;
    private final MoneyInService moneyInService;
    private final NotificationService notifyService;

    @Autowired
    public FinancialController(ContractService contractService, PremiumService premiumService, MoneyInService moneyInService, NotificationService notifyService) {
        this.contractService = contractService;
        this.premiumService = premiumService;
        this.moneyInService = moneyInService;
        this.notifyService = notifyService;
    }

    @GetMapping(value = "/premiums/create/contract/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String createPremium(@ModelAttribute(name = "premiumModel") PremiumModel premiumModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage(CANNOT_FIND_CONTRACT + id);
            return "redirect:/";
        }

        premiumModel = this.premiumService.createForView(contract);
        model.addAttribute("premiumModel", premiumModel);

        return "financial/premium/create-premium";
    }

    @GetMapping(value = "/money-ins/create/contract/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String createMoneyIn(@ModelAttribute(name = "moneyInModel") MoneyInModel moneyInModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage(CANNOT_FIND_CONTRACT + id);
            return "redirect:/";
        }

        moneyInModel = this.moneyInService.createForView(contract);
        model.addAttribute("moneyInModel", moneyInModel);

        return "financial/money-in/create-money-in";
    }
    @PostMapping(value = "/premiums/create/contract/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String createPremiumValidate(@Valid PremiumModel premiumModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, @PathVariable("id") Long id) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage(CANNOT_FIND_CONTRACT + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            return "redirect:/contracts/" + id;
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "financial/premium/create/create-premium";
        }
        Premium premium = DTOConvertUtil.convert(premiumModel, Premium.class);
        premiumService.create(contract, premium);
        MoneyIn moneyIn = this.moneyInService.findOldestPendingMoneyIn(contract);
        if (moneyIn != null) {
            premiumService.tryToPay(premium, moneyIn);
            notifyService.addWarningMessage(String.format(PREMIUM_WITH_ID_S_WAS_CREATED_AND_MONEY_IN_WITH_ID_S_WAS_APPLIED_TO_IT,premium.getId(),moneyIn.getId()));
        } else
            notifyService.addInfoMessage(String.format(PREMIUM_WITH_ID_S_WAS_CREATED,premium.getId()));
        return "redirect:/contracts/" + id;
    }

    @PostMapping(value = "/money-ins/create/contract/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String createMoneyInValidate(@Valid MoneyInModel moneyInModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, @PathVariable("id") Long id) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage(CANNOT_FIND_CONTRACT + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            return "redirect:/contracts/" + id;
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "financial/money-in/create/create-money-in";
        }
        MoneyIn moneyIn = DTOConvertUtil.convert(moneyInModel, MoneyIn.class);
        moneyInService.create(contract, moneyIn);
        Premium premium = this.premiumService.findOldestPendingPremium(contract);
        if (premium != null) {
            premiumService.tryToPay(premium, moneyIn);
            notifyService.addWarningMessage(String.format(MONEY_IN_WITH_ID_S_WAS_CREATED_AND_PREMIUM_WITH_ID_S_WAS_PAID_WITH_IT,moneyIn.getId(),premium.getId()));
        } else
            notifyService.addInfoMessage(String.format(MONEY_IN_WITH_ID_S_WAS_CREATED, moneyIn.getId() ));
        return "redirect:/contracts/" + id;
    }
}
