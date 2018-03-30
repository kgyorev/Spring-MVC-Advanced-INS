package com.insurance.ins.business.controllers;

import com.insurance.ins.business.models.CreateContractForm;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ContractsController {
    public static final String CLIENT = "siteClient";
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private ContractService contractService;
    @Autowired
//    private DistributorService distributorService;
//    @Autowired
    private PersonService personService;
    @Autowired
    private NotificationService notifyService;
//    @RequestMapping("/contracts/view/{id}")
//    public String view(@PathVariable("id") Long id, Model model) {
//        Contract contract = contractService.findById(id);
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//        Client client = contract.getClient();
//        Long clientId=client.getId();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String startdt = df.format(contract.getStartdt());
//        String enddt = df.format(contract.getEnddt());
//        model.addAttribute("enddt", enddt);
//        model.addAttribute("startdt", startdt);
//        model.addAttribute("clientId", clientId);
//        model.addAttribute("contract", contract);
//        double premiumAmount_tmp = contract.getPremiumamount();
//        DecimalFormat dfDec = new DecimalFormat("#.##");
//        String premiumAmount = String.valueOf(dfDec.format(premiumAmount_tmp));
//
//        model.addAttribute("premiumAmount", premiumAmount);
//        return "contracts/index";
//    }
//    @RequestMapping(value="/contracts",method = RequestMethod.GET)
//    public String view_all(SearchContractForm searchContractForm, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        List<Contract> contractall = contractService.findAll();
//        model.addAttribute("contractall", contractall);
//        return "contracts/index_all";
//    }
//    @RequestMapping(value="/contracts",method = RequestMethod.POST)
//    public String searchContract(SearchContractForm searchContractForm, Model model) {
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
//    public String cancel(SearchContractForm searchContractForm, @PathVariable("id") Long id, Model model) {
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
//    public String inforce(SearchContractForm searchContractForm, @PathVariable("id") Long id, Model model) {
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
//    @RequestMapping(value ="/contracts/edit/{id}", method = RequestMethod.GET)
//    public String editPage(CreateContractForm createContractForm, @PathVariable("id") Long id, Model model) {
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
//        Client client = contract.getClient();
//        model.addAttribute("contract", contract);
//        Long clientId=client.getId();
//        model.addAttribute("clientId", clientId);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String startdt = df.format(contract.getStartdt());
//        model.addAttribute("startdt", startdt);
//        return "contracts/edit";
//    }
//    @RequestMapping(value ="/contracts/edit/{id}", method = RequestMethod.POST)
//    public String edit(@Valid CreateContractForm createContractForm, BindingResult bindingResult, @PathVariable("id") Long id, Model model) throws ParseException {
//        Contract contract = contractService.findById(id);
//        Client client = contract.getClient();
//        if (contract == null) {
//            notifyService.addErrorMessage("Cannot find contract #" + id);
//            return "redirect:/";
//        }
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            model.addAttribute("contract", contract);
//            Long clientId=client.getId();
//            model.addAttribute("clientId", clientId);
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            String startdt = df.format(contract.getStartdt());
//            model.addAttribute("startdt", startdt);
//            return "/contracts/edit";
//        }
//        String startdt = createContractForm.getStartdt();
//        int duration = createContractForm.getDuration();
//        double amount=createContractForm.getAmount();
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
//
//        contractService.edit(contract);
//        notifyService.addInfoMessage("Edit successful");
//        return "redirect:/contracts";
//    }
    @RequestMapping(value ="/contracts/create", method = RequestMethod.GET)
    public String createPage(CreateContractForm createContractForm) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
           return "business/contracts/create-contract";
    }
//    @RequestMapping(value ="/contracts/create", method = RequestMethod.POST)
//    public String create(@Valid CreateContractForm createContractForm, BindingResult bindingResult)  throws ParseException {
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            return "/contracts/create";
//        }
//        Contract contract=new Contract();
//        Object usernameObject = httpSession.getAttribute(USER_LOGIN);
//
//        if (usernameObject!=null)
//        {
//            String username = usernameObject.toString();
//            List<Distributor> userall = distributorService.findAll();
//            Stream<Distributor> userstream = userall.stream().filter(u->u.getUsername().equals(username));
//            List<Distributor> userslist = userstream.collect(Collectors.toList());
//            Distributor distributor = userslist.get(0);
//            contract.setDistributor(distributor);
//        }
//
//        String id_str = createContractForm.getClientId();
//        String startdt = createContractForm.getStartdt();
//        int duration = createContractForm.getDuration();
//        double amount=createContractForm.getAmount();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDate = df.parse(startdt);
//        contract.setStartdt(startDate);
//        contract.setAmount(amount);
//        long id= Long.parseLong(id_str);
//
//        Client client = clientService.findById(id);
//        if (client==null) {
//            notifyService.addErrorMessage("Client not found!");
//            return "/contracts/create";
//        }
//        contract.setClient(client);
//        contract.setDuration(duration);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startDate);
//        cal.add(Calendar.YEAR, duration);
//        cal.add(Calendar.DATE, -1);
//        Date endDate = cal.getTime();
//        contract.setEnddt(endDate);
//        contract.setStatus("In Force");
//        int age=client.getAge(startDate)+1;
//        Double premiumamount = age*amount/(duration*12*100);
//        contract.setPremiumamount(premiumamount);
//        contractService.create(contract);
//        notifyService.addInfoMessage("Contract with Id: "+contract.getId()+" was created.");
//        return "redirect:/";
//    }
}
