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
//@RequestMapping("/nuggets")
@CrossOrigin(origins = "http://localhost:63343")
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
    public List<PremiumModel> allPremiumsByClient(@RequestParam("egn") String egn) {
        List<Premium> allByStatusAndOwnerEgn = this.premiumService.findAllByStatusAndContract_OwnerEgn(com.insurance.ins.financial.enums.Status.PENDING, egn);
        List<PremiumModel> premiumModels=new ArrayList<>();
        for (Premium premium : allByStatusAndOwnerEgn) {
            premiumModels.add(DTOConvertUtil.convert(premium,PremiumModel.class));
        }
        return premiumModels;

    }
//    @GetMapping(value="/watches/top",produces = "application/json")
//    @ResponseBody
//    public List<Watch> top() {
//        List<Watch> all = this.watchService.findFirst4ByView();
//        return all;
//
//    }
//
//    @GetMapping(value="/watches/details",produces = "application/json")
//    @ResponseBody
//    public Watch one(@RequestParam("id") Long id) {
//
//        Watch one = this.watchService.findById(id);
//        watchService.viewIncrement(one);
//          return one;
//
//    }
//
//    @PostMapping(value="/watches/add",produces = "application/json")
//    @ResponseBody
////    public Watch add( HttpServletRequest request) {
////        Map<String, String[]> parameterMap = request.getParameterMap();
//    public Watch add(
//            @RequestParam("name") String name,
//            @RequestParam("price") String price,
//            @RequestParam("image") String image,
//            @RequestParam("description") String description) {
//        Watch one = this.watchService.create(name,price,image,description);
//        return one;
//
//    }

}
