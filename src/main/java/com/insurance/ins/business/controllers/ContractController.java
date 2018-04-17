package com.insurance.ins.business.controllers;

import com.insurance.ins.business.batch.BusinessBatch;
import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.entites.Product;
import com.insurance.ins.business.enums.Status;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.contract.ContractModel;
import com.insurance.ins.business.models.contract.SearchContractModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.models.AllMoneyInsViewModel;
import com.insurance.ins.financial.models.AllPremiumsViewModel;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.prsnorg.entites.prsn.Person;
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

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;

@Controller
public class ContractController {
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
    public String view(@ModelAttribute(name = "contractModel") ContractModel contractModel, @PathVariable("id") Long id, Model model,@PageableDefault(size = 10) Pageable page) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
//        if(contractModel==null){
        String selectedTab = contractModel.getSelectedTab();
        contractModel = DTOConvertUtil.convert(contract, ContractModel.class);
        contractModel.setSelectedTab(selectedTab);
//        }
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

    //    @RequestMapping(value="/contracts",method = RequestMethod.POST)
//    public String searchContract(SearchContractModel searchContractForm, Model model) {
//        Long id = searchContractForm.getCntrctId();
//        List<Contract> allcontracts = contractService.findAll();
//        List<Contract> contractall;
//        if(id!=null) {
//            Stream<Contract> contractStream = allcontracts.stream().filter(u -> u.getId().equals(id));
//            List<Contract> clientall_tmp = contractStream.collect(Collectors.toList());
//            if(clientall_tmp.size()==0)
//            {
//                notifyService.addWarningMessage("Cannot find contract with Id: " + id);
//                contractall= allcontracts;
//            }
//            else
//                contractall = clientall_tmp;
//        }
//        else
//            contractall= allcontracts;
//        model.addAttribute("contractall", contractall);
//        return "contracts/index_all";
//    }
//    @RequestMapping(value ="/contracts/delete/{id}", method = RequestMethod.GET)
//    public String delete(@PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//       contractService.deleteById(id);
//        List<Contract> contractall = contractService.findAll();
//        model.addAttribute("contractall", contractall);
//        return "contracts/index_all";  // v primera e view.html
//    }
//    @RequestMapping(value ="/contracts/cancel/{id}", method = RequestMethod.GET)
//    public String cancel(SearchContractModel searchContractForm, @PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract with Id: " + id);
//            return "redirect:/";
//        }
//        notifyService.addInfoMessage("Contract with Id: " + id + " was canceled");
//        contractService.cancel(contract);
//        List<Contract> contractall = contractService.findAll();
//        model.addAttribute("contractall", contractall);
//        return "contracts/index_all";  // v primera e view.html
//    }
//    @RequestMapping(value ="/contracts/inforce/{id}", method = RequestMethod.GET)
//    public String inforce(SearchContractModel searchContractForm, @PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addWarningMessage("Cannot find contract with Id: " + id);
//            return "redirect:/";
//        }
//        notifyService.addInfoMessage("Contract with Id: " + id + " was Reactivated");
//        contractService.inForce(contract);
//        List<Contract> contractall = contractService.findAll();
//        model.addAttribute("contractall", contractall);
//        return "contracts/index_all";  // v primera e view.html
//    }
    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "contractModel") ContractModel contractModel, @PathVariable("id") Long id, Model model) {
        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }

        contractModel = DTOConvertUtil.convert(contract, ContractModel.class);
        model.addAttribute("contractModel", contractModel);
        return "business/contract/edit-contract";
    }

    @RequestMapping(value = "/contracts/confirm/edit/{id}", method = RequestMethod.GET)
    public String confirmEdit(@Valid @ModelAttribute(name = "contractModel") ContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/contracts";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/business/contract/edit-contract";
        }
        String productIdntfr = contractModel.getProduct();
        Product product = productService.findByIdntfr(productIdntfr);
        if (product == null) {
            notifyService.addErrorMessage("Product not found!");
            return "/business/contract/edit-contract";
        }

        String DistributorIdStr = contractModel.getDistributor();
        long DistributorId = Long.parseLong(DistributorIdStr);
        Distributor distributor = distributorService.findById(DistributorId);
        if (distributor == null) {
            notifyService.addErrorMessage("Distributor not found!");
            return "/business/contract/edit-contract";
        }

        Contract contract = contractService.findById(id);
        if (contract == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        String id_str = contractModel.getOwner();
        long personId = Long.parseLong(id_str);
        Person person = personService.findById(personId);
        int age = person.getAge(contractModel.getStartDt());
        Double premiumAmount = age * contractModel.getAmount() / (contractModel.getDuration() * 12 * 100);
        contractModel.setPremiumAmount(Math.round(premiumAmount * 100.0) / 100.0);
        return "business/contract/confirm-edit-contract";
    }

    @RequestMapping(value = "/contracts/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid ContractModel contractModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "/business/contract/edit-contract";
        }
//        Contract contract = contractService.findById(id);
//        Client client = contract.getClient();
        if (contractModel == null) {
            notifyService.addErrorMessage("Cannot find contract #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/business/contract/edit-contract";
        }
//        String startdt = contractModel.getStartdt();
//        int duration = contractModel.getDuration();
//        double amount=contractModel.getAmount();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDate = df.parse(startdt);
//        contract.setStartdt(startDate);
//        contract.setAmount(amount);
//        contract.setDuration(duration);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startDate);
//        cal.add(Calendar.YEAR, duration);
//        cal.add(Calendar.DATE, -1);
//        Date endDate = cal.getTime();
//        contract.setEnddt(endDate);
//        int age=client.getAge(startDate)+1;
//        Double premiumamount = age*amount/(duration*12*100);
//        contract.setPremiumamount(premiumamount);
        contractService.edit(contractModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/contracts";
    }

    @RequestMapping(value = "/contracts/create", method = RequestMethod.GET)
    public String createPage(@ModelAttribute(name = "contractModel") ContractModel contractMode) {
        return "business/contract/create-contract";
    }


    @RequestMapping(value = "/contracts/confirm/create", method = RequestMethod.GET)
    public String confirmCreate(@Valid @ModelAttribute(name = "contractModel") ContractModel contractModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/business/contract/create-contract";
        }
        String id_str = contractModel.getOwner();
        long id = Long.parseLong(id_str);
        Person person = personService.findById(id);
        String productIdntfr = contractModel.getProduct();
        Product product = productService.findByIdntfr(productIdntfr);
        if (product == null) {
            notifyService.addErrorMessage("Product not found!");
            return "/business/contract/create-contract";
        }
        Contract contract = DTOConvertUtil.convert(contractModel, Contract.class);
        contract.setOwner(person);
       if(!productService.checkProductRules(product,contract)){
           return "/business/contract/create-contract";
       }
        int age = person.getAge(contractModel.getStartDt()) + 1;
        Double premiumAmount = age * contractModel.getAmount() / (contractModel.getDuration() * 12 * 100);
        contractModel.setPremiumAmount(Math.round(premiumAmount * 100.0) / 100.0);
         return "business/contract/confirm-create-contract";
    }


    @RequestMapping(value = "/contracts/create", method = RequestMethod.POST)
    public String create(@Valid ContractModel contractModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "/business/contract/create-contract";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/business/contract/create-contract";
        }
        Contract contract = DTOConvertUtil.convert(contractModel, Contract.class);
        String id_str = contractModel.getOwner();
        long id = Long.parseLong(id_str);
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Owner not found!");
            return "/business/contract/create-contract";
        }
        String productIdntfr = contractModel.getProduct();
        Product product = productService.findByIdntfr(productIdntfr);
        if (product == null) {
            notifyService.addErrorMessage("Product not found!");
            return "/business/contract/create-contract";
        }
        String DistributorIdStr = contractModel.getDistributor();
        long DistributorId = Long.parseLong(DistributorIdStr);
        Distributor distributor = distributorService.findById(DistributorId);
        if (distributor == null) {
            notifyService.addErrorMessage("Distributor not found!");
            return "/business/contract/create-contract";
        }
        contract.setOwner(person);
        if(!productService.checkProductRules(product,contract)){
            return "/business/contract/create-contract";
        }
        contract.setProduct(product);
        contract.setDistributor(distributor);
        LocalDate endDate = contractModel.getStartDt().plusYears(contract.getDuration());
        contract.setEndDt(endDate);
        contract.setCreationDt(LocalDate.now());
        contract.setStatus(Status.IN_FORCE);
        int age = person.getAge(contract.getStartDt()) + 1;
        Double premiumAmount = age * contract.getAmount() / (contract.getDuration() * 12 * 100);
        contract.setPremiumAmount(Math.round(premiumAmount * 100.0) / 100.0);
        contractService.create(contract);
        notifyService.addInfoMessage("Contract with Id: " + contract.getId() + " was created.");
        return "redirect:/";
    }
}
