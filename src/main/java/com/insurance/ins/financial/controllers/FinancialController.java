package com.insurance.ins.financial.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.MoneyIn;
import com.insurance.ins.financial.Premium;
import com.insurance.ins.financial.models.MoneyInModel;
import com.insurance.ins.financial.models.PremiumModel;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class FinancialController {
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
    private PremiumService premiumService;
    @Autowired
    private MoneyInService moneyInService;
    @Autowired
    private NotificationService notifyService;

//    @RequestMapping(value = "/contracts/{id}", method = RequestMethod.GET)
//    public String view(@ModelAttribute(name = "contractModel") ContractModel contractModel, @PathVariable("id") Long id, Model model) {
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//        contractModel = DTOConvertUtil.convert(contract, ContractModel.class);
//        model.addAttribute("contractModel", contractModel);
//        return "business/contract/view-contract";
//    }
//
//    @RequestMapping(value = "/contracts", method = RequestMethod.GET)
//    public String view_all(@ModelAttribute(name = "searchContractModel") SearchContractModel searchContractModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
//        AllContractsViewModel contractall = contractService.searchContract(searchContractModel, pageable);
//
//        if (
//                (!searchContractModel.getCntrctId().equals("") || !(searchContractModel.getStatus() == null))
//                        && !contractall.getContracts().hasContent()) {
//            notifyService.addWarningMessage("Cannot find contracts with given search criteria.");
//        }
//        model.addAttribute("contractall", contractall);
//        return "business/contract/search-contract";
//    }
//
//    //    @RequestMapping(value="/contracts",method = RequestMethod.POST)
////    public String searchContract(SearchContractModel searchContractForm, Model model) {
////        Long id = searchContractForm.getCntrctId();
////        List<Contract> allcontracts = contractService.findAll();
////        List<Contract> contractall;
////        if(id!=null) {
////            Stream<Contract> contractStream = allcontracts.stream().filter(u -> u.getId().equals(id));
////            List<Contract> clientall_tmp = contractStream.collect(Collectors.toList());
////            if(clientall_tmp.size()==0)
////            {
////                notifyService.addWarningMessage("Cannot find contract with Id: " + id);
////                contractall= allcontracts;
////            }
////            else
////                contractall = clientall_tmp;
////        }
////        else
////            contractall= allcontracts;
////        model.addAttribute("contractall", contractall);
////        return "contracts/index_all";
////    }
////    @RequestMapping(value ="/contracts/delete/{id}", method = RequestMethod.GET)
////    public String delete(@PathVariable("id") Long id, Model model) {
////        Object user = httpSession.getAttribute(USER_LOGIN);
////        if (user == null) {
////            notifyService.addErrorMessage("Please Login!");
////            return "redirect:/";
////        }
////        Contract contract = contractService.findById(id);
////        if (contract == null) {
////            notifyService.addErrorMessage("Cannot find contract #" + id);
////            return "redirect:/";
////        }
////       contractService.deleteById(id);
////        List<Contract> contractall = contractService.findAll();
////        model.addAttribute("contractall", contractall);
////        return "contracts/index_all";  // v primera e view.html
////    }
////    @RequestMapping(value ="/contracts/cancel/{id}", method = RequestMethod.GET)
////    public String cancel(SearchContractModel searchContractForm, @PathVariable("id") Long id, Model model) {
////        Object user = httpSession.getAttribute(USER_LOGIN);
////        if (user == null) {
////            notifyService.addErrorMessage("Please Login!");
////            return "redirect:/";
////        }
////        Contract contract = contractService.findById(id);
////        if (contract == null) {
////            notifyService.addErrorMessage("Cannot find contract with Id: " + id);
////            return "redirect:/";
////        }
////        notifyService.addInfoMessage("Contract with Id: " + id + " was canceled");
////        contractService.cancel(contract);
////        List<Contract> contractall = contractService.findAll();
////        model.addAttribute("contractall", contractall);
////        return "contracts/index_all";  // v primera e view.html
////    }
////    @RequestMapping(value ="/contracts/inforce/{id}", method = RequestMethod.GET)
////    public String inforce(SearchContractModel searchContractForm, @PathVariable("id") Long id, Model model) {
////        Object user = httpSession.getAttribute(USER_LOGIN);
////        if (user == null) {
////            notifyService.addErrorMessage("Please Login!");
////            return "redirect:/";
////        }
////        Contract contract = contractService.findById(id);
////        if (contract == null) {
////            notifyService.addWarningMessage("Cannot find contract with Id: " + id);
////            return "redirect:/";
////        }
////        notifyService.addInfoMessage("Contract with Id: " + id + " was Reactivated");
////        contractService.inForce(contract);
////        List<Contract> contractall = contractService.findAll();
////        model.addAttribute("contractall", contractall);
////        return "contracts/index_all";  // v primera e view.html
////    }
//    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.GET)
//    public String editPage(@ModelAttribute(name = "contractModel") ContractModel contractModel, @PathVariable("id") Long id, Model model) {
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//
//        contractModel = DTOConvertUtil.convert(contract, ContractModel.class);
//        model.addAttribute("contractModel", contractModel);
//
////        Client client = contract.getClient();
////        model.addAttribute("contract", contract);
////        Long clientId=client.getId();
////        model.addAttribute("clientId", clientId);
////        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
////        String startdt = df.format(contract.getStartdt());
////        model.addAttribute("startdt", startdt);
//        return "business/contract/edit-contract";
//    }
//
//    @RequestMapping(value = "/contracts/confirm/edit/{id}", method = RequestMethod.GET)
//    public String confirmEdit(@Valid @ModelAttribute(name = "contractModel") ContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
//        if (action.equals("return")) {
//            return "redirect:/contracts";
//        }
//        String productIdntfr = contractModel.getProduct();
//        Product product = productService.findByIdntfr(productIdntfr);
//        if (product == null) {
//            notifyService.addErrorMessage("Product not found!");
//            return "/business/contract/edit-contract";
//        }
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            return "/business/contract/edit-contract";
//        }
////        String startdt = contractModel.getStartdt();
////        int duration = contractModel.getDuration();
////        double amount=contractModel.getAmount();
////        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
////        Date startDate = df.parse(startdt);
////        contract.setStartdt(startDate);
////        contract.setAmount(amount);
////        contract.setDuration(duration);
////        Calendar cal = Calendar.getInstance();
////        cal.setTime(startDate);
////        cal.add(Calendar.YEAR, duration);
////        cal.add(Calendar.DATE, -1);
////        Date endDate = cal.getTime();
////        contract.setEnddt(endDate);
////        int age=client.getAge(startDate)+1;
////        Double premiumamount = age*amount/(duration*12*100);
////        contract.setPremiumamount(premiumamount);
////
////        contractService.edit(contract);
////        notifyService.addInfoMessage("Edit successful");
//        String id_str = contractModel.getOwner();
//        long personId = Long.parseLong(id_str);
//        Person person = personService.findById(personId);
//        int age = person.getAge(contractModel.getStartDt()) + 1;
//        Double premiumAmount = age * contractModel.getAmount() / (contractModel.getDuration() * 12 * 100);
//        contractModel.setPremiumAmount(premiumAmount);
//        return "business/contract/confirm-edit-contract";
//    }
//
//    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.POST)
//    public String edit(@Valid ContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
//        if (action.equals("return")) {
//            return "/business/contract/edit-contract";
//        }
////        Contract contract = contractService.findById(id);
////        Client client = contract.getClient();
//        if (contractModel == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            return "/business/contract/edit-contract";
//        }
////        String startdt = contractModel.getStartdt();
////        int duration = contractModel.getDuration();
////        double amount=contractModel.getAmount();
////        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
////        Date startDate = df.parse(startdt);
////        contract.setStartdt(startDate);
////        contract.setAmount(amount);
////        contract.setDuration(duration);
////        Calendar cal = Calendar.getInstance();
////        cal.setTime(startDate);
////        cal.add(Calendar.YEAR, duration);
////        cal.add(Calendar.DATE, -1);
////        Date endDate = cal.getTime();
////        contract.setEnddt(endDate);
////        int age=client.getAge(startDate)+1;
////        Double premiumamount = age*amount/(duration*12*100);
////        contract.setPremiumamount(premiumamount);
//        contractService.edit(contractModel);
//        notifyService.addInfoMessage("Edit successful");
//        return "redirect:/contracts";
//    }

    @RequestMapping(value = "/contracts/create/premium/{id}", method = RequestMethod.GET)
    public String createPremium(@ModelAttribute(name = "premiumModel") PremiumModel premiumModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }

        premiumModel = this.premiumService.createForView(contract);
        MoneyIn moneyIn= this.moneyInService.findOldestPendingMoneyIn(contract);
        MoneyInModel moneyInModel=null;
        if(moneyIn!=null){
            moneyInModel = DTOConvertUtil.convert(moneyIn,MoneyInModel.class);
        }
        model.addAttribute("moneyInModel", moneyInModel);
        model.addAttribute("premiumModel", premiumModel);

        return "financial/premium/create-premium";
    }
    @RequestMapping(value = "/contracts/create/money-in/{id}", method = RequestMethod.GET)
    public String createMoneyIn(@ModelAttribute(name = "moneyInModel") MoneyInModel moneyInModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }

        moneyInModel = this.moneyInService.createForView(contract);
        model.addAttribute("moneyInModel", moneyInModel);

        return "financial/money-in/create-money-in";
    }
    //
//    @RequestMapping(value = "/contracts/confirm/create", method = RequestMethod.GET)
//    public String confirmCreate(@Valid @ModelAttribute(name = "contractModel") ContractModel contractModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
//        if (action.equals("return")) {
//            return "redirect:/";
//        }
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            return "/business/contract/create-contract";
//        }
////        Contract contract = DTOConvertUtil.convert(contractModel, Contract.class);
//        String id_str = contractModel.getOwner();
//        long id = Long.parseLong(id_str);
////
//        Person person = personService.findById(id);
////        if (person == null) {
////            notifyService.addErrorMessage("Owner not found!");
////            return "/business/contract/create-contract";
////        }
////        contract.setOwner(person);
//        String productIdntfr = contractModel.getProduct();
//        Product product = productService.findByIdntfr(productIdntfr);
//        if (product == null) {
//            notifyService.addErrorMessage("Product not found!");
//            return "/business/contract/create-contract";
//        }
////        contract.setProduct(product);
////        Distributor distributor = distributorService.findById(1L);
////        contract.setDistributor(distributor);
////        LocalDate endDate = contractModel.getStartDt().plusYears(contract.getDuration());
////        contract.setEndDt(endDate);
////        contract.setCreationDt(LocalDate.now());
////        contract.setStatus(Status.IN_FORCE);
//        int age = person.getAge(contractModel.getStartDt()) + 1;
//        Double premiumAmount = age * contractModel.getAmount() / (contractModel.getDuration() * 12 * 100);
//        contractModel.setPremiumAmount(premiumAmount);
////        contractService.create(contract);
////        notifyService.addInfoMessage("Contract with Id: " + contract.getId() + " was created.");
//        return "business/contract/confirm-edit-contract";
//    }
//
//
    @RequestMapping(value = "/contracts/create/premium/{id}", method = RequestMethod.POST)
    public String createPremiumValidate(@Valid PremiumModel premiumModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, @PathVariable("id") Long id) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            return "redirect:/contracts/" + id;
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/financial/premium/create/create-premium";
        }
        Premium premium = DTOConvertUtil.convert(premiumModel, Premium.class);
        premiumService.create(contract,premium);
        MoneyIn moneyIn= this.moneyInService.findOldestPendingMoneyIn(contract);
        if(moneyIn!=null){
            premiumService.pay(premium,moneyIn);
        }
        notifyService.addInfoMessage("Premium with Id: " + premium.getId() + " was created.");
        return "redirect:/contracts/" + id;
    }
    @RequestMapping(value = "/contracts/create/money-in/{id}", method = RequestMethod.POST)
    public String createMoneyInValidate(@Valid MoneyInModel moneyInModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, @PathVariable("id") Long id) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        if (action.equals("return")) {
            return "redirect:/contracts/" + id;
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/financial/money-in/create/create-money-in";
        }
        MoneyIn moneyIn = DTOConvertUtil.convert(moneyInModel, MoneyIn.class);
        moneyInService.create(contract,moneyIn);
        notifyService.addInfoMessage("Money In with Id: " + moneyIn.getId() + " was created.");
        return "redirect:/contracts/" + id;
    }
}
