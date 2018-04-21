package com.insurance.ins.business.controllers;


import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.contract.ContractModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.models.PremiumModel;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.utils.DTOConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller

public class RestContractController {


    private final ContractService contractService;
    private final PremiumService premiumService;
    @Autowired
    public RestContractController(ContractService contractService, PremiumService premiumService) {
        this.contractService = contractService;
        this.premiumService = premiumService;
    }

    @GetMapping(value="/contracts/client",produces = "application/json")
    @ResponseBody
    @CrossOrigin
    public List<ContractModel> allContractsByClient(@RequestParam("egn") String egn) {
        List<Contract> allByStatusAndOwnerEgn = this.contractService.findAllByStatusAndOwnerEgn(Status.IN_FORCE, egn);
        List<ContractModel> contractModels=new ArrayList<>();
        for (Contract contract : allByStatusAndOwnerEgn) {
            contractModels.add(DTOConvertUtil.convert(contract,ContractModel.class));
        }
        return contractModels;

    }
    @GetMapping(value="/premiums/client",produces = "application/json")
    @ResponseBody
    @CrossOrigin
    public List<PremiumModel> allPremiumsByClient(@RequestParam("egn") String egn) {
        List<Premium> allByStatusAndOwnerEgn = this.premiumService.findAllByStatusAndContract_OwnerEgn(com.insurance.ins.financial.enums.Status.PENDING, egn);
        List<PremiumModel> premiumModels=new ArrayList<>();
        for (Premium premium : allByStatusAndOwnerEgn) {
            PremiumModel premiumModel = DTOConvertUtil.convert(premium, PremiumModel.class);
            premiumModel.setCntrctId(String.valueOf(premium.getContract().getId()));
            premiumModels.add(premiumModel);
        }
        return premiumModels;
    }

}
