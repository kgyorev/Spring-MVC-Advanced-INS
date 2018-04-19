package com.insurance.ins.business.controllers;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.distributor.AllDistributorsViewModel;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.models.distributor.SearchDistributorModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.business.services.ProductService;
import com.insurance.ins.financial.services.MoneyInService;
import com.insurance.ins.financial.services.PremiumService;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.services.UserService;
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
public class DistributorController {
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
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PremiumService premiumService;
    @Autowired
    private MoneyInService moneyInService;
    @Autowired
    private NotificationService notifyService;

    @RequestMapping(value = "/distributors/{id}", method = RequestMethod.GET)
    public String view(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel, @PathVariable("id") Long id, Model model, @PageableDefault(size = 10) Pageable page) {
        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage("Cannot find distributor #" + id);
            return "redirect:/";
        }
        String selectedTab = distributorModel.getSelectedTab();
        distributorModel = DTOConvertUtil.convert(distributor, DistributorModel.class);
        distributorModel.setSelectedTab(selectedTab);
        AllContractsViewModel contractAll = contractService.searchContractsForDistributor(distributor, page);
        model.addAttribute("contractAll", contractAll);

        model.addAttribute("distributorModel", distributorModel);

        return "business/distributor/view-distributor";
    }

    @RequestMapping(value = "/distributors", method = RequestMethod.GET)
    public String view_all(@ModelAttribute(name = "searchDistributorModel") SearchDistributorModel searchDistributorModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllDistributorsViewModel distributorall = distributorService.searchDistributor(searchDistributorModel, pageable);

        if (
                (!searchDistributorModel.getReferenceId().equals("") || !(searchDistributorModel.getReferenceId() == null))
                        && !distributorall.getDistributors().hasContent()) {
            notifyService.addWarningMessage("Cannot find distributors with given search criteria.");
        }
        model.addAttribute("distributorall", distributorall);
        return "business/distributor/search-distributor";
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
    @RequestMapping(value = "/distributors/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel, @PathVariable("id") Long id, Model model) {
        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage("Cannot find distributor #" + id);
            return "redirect:/";
        }
        distributorModel = DTOConvertUtil.convert(distributor, DistributorModel.class);
        model.addAttribute("distributorModel", distributorModel);
        return "business/distributor/edit-distributor";
    }

    @RequestMapping(value = "/distributors/confirm/edit/{id}", method = RequestMethod.GET)
    public String confirmEdit(@Valid @ModelAttribute(name = "distributorModel") DistributorModel distributorModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/distributors";
        }
        String organizationId= distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage("Reference organization not found!");
            return "business/distributor/edit-distributor";
        }

        String userId= distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage("Reference user not found!");
            return "business/distributor/edit-distributor";
        }

        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage("Cannot find distributor #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/distributor/edit-distributor";
        }
        return "business/distributor/confirm-edit-distributor";
    }

    @RequestMapping(value = "/distributors/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid DistributorModel distributorModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "business/distributor/edit-distributor";
        }
        if (distributorModel == null) {
            notifyService.addErrorMessage("Cannot find distributor #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/distributor/edit-distributor";
        }

        String organizationId= distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage("Reference organization not found!");
            return "business/distributor/edit-distributor";
        }

        String userId= distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage("Reference user not found!");
            return "business/distributor/edit-distributor";
        }
        distributorService.edit(distributorModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/distributors";
    }

    @RequestMapping(value = "/distributors/create", method = RequestMethod.GET)
    public String createPage(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel) {
        return "business/distributor/create-distributor";
    }


    @RequestMapping(value = "/distributors/confirm/create", method = RequestMethod.GET)
    public String confirmCreate(@Valid @ModelAttribute(name = "distributorModel") DistributorModel distributorModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/distributor/create-distributor";
        }

        String organizationId= distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage("Reference organization not found!");
            return "business/distributor/create-distributor";
        }

        String userId= distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage("Reference user not found!");
            return "business/distributor/create-distributor";
        }
        return "business/distributor/confirm-create-distributor";
    }


    @RequestMapping(value = "/distributors/create", method = RequestMethod.POST)
    public String create(@Valid DistributorModel distributorModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "business/distributor/create-distributor";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "business/distributor/create-distributor";
        }
        Distributor distributor = DTOConvertUtil.convert(distributorModel, Distributor.class);


        String organizationId= distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage("Reference organization not found!");
            return "business/distributor/create-distributor";
        }

        String userId= distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage("Reference user not found!");
            return "business/distributor/create-distributor";
        }


        distributor.setOrganization(organization);
        distributor.setUser(user);
        distributorService.create(distributor);
        notifyService.addInfoMessage("Distributor with Id: " + distributor.getId() + " was created.");
        return "redirect:/";
    }
}
