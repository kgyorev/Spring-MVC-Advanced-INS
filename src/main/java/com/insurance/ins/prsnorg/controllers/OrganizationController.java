package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.models.AllOrganizationsViewModel;
import com.insurance.ins.prsnorg.entites.org.models.OrganizationModel;
import com.insurance.ins.prsnorg.entites.org.models.SearchOrganizationModel;
import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class OrganizationController {
    private final HttpSession httpSession;
    private final OrganizationService organizationService;
    private final NotificationService notifyService;

    @Autowired
    public OrganizationController(NotificationService notifyService, HttpSession httpSession, OrganizationService organizationService) {
        this.notifyService = notifyService;
        this.httpSession = httpSession;
        this.organizationService = organizationService;
    }
    @GetMapping(value = "/organizations")
    public String view_all(@ModelAttribute(name = "searchOrganizationModel") SearchOrganizationModel searchOrganizationModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllOrganizationsViewModel organizationAll =  organizationService.searchOrganization(searchOrganizationModel,pageable);

        if((!searchOrganizationModel.getVat().equals("")||!searchOrganizationModel.getFullName().equals(""))
                &&!organizationAll.getOrganizations().hasContent()) {
                notifyService.addWarningMessage("Cannot find clients with given search criteria.");
            }
        model.addAttribute("organizationAll", organizationAll);
        return "prsnorg/org/search-organization";
    }

    //    @PostMapping(value="/organizations")
//    public String searchClient(SearchOrganizationModel searchOrganizationModel, Model model) {
//        String egn = searchOrganizationModel.getVat();
//        List<Organization> allclients = organizationService.findAll();
//        List<Organization> clientall;
//        if(!egn.equals("")) {
//            Stream<Organization> userstream = allclients.stream().filter(u -> u.getVat().equals(egn));
//            List<Organization> clientall_tmp = userstream.collect(Collectors.toList());
//            if(clientall_tmp.size()==0)
//            {
//                notifyService.addWarningMessage("Cannot find client with EGN: " + egn);
//                clientall= allclients;
//            }
//            else
//                clientall = clientall_tmp;
//        }
//        else
//          clientall= allclients;
//        model.addAttribute("clientall", clientall);
//        return "prsnorg/prsn/search-organization";
//    }
//    @RequestMapping(value ="/clients/delete/{id}", method = RequestMethod.GET)
//    public String delete(SearchClientForm searchClientForm, @PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Client client = organizationService.findById(id);
//        if (client == null) {
//            notifyService.addErrorMessage("Cannot find client #" + id);
//            return "redirect:/";
//        }
//        Set<Contract> contracts = client.getContracts();
//        int contracts_count = contracts.size();
//        if (contracts_count != 0) {
//            notifyService.addErrorMessage("Cannot delete client #" + id + " ,it has contracts.");
//            List<Client> clientall = organizationService.findAll();
//            model.addAttribute("clientall", clientall);
//            return "clients/index_all";
//        }
//       organizationService.deleteById(id);
//        List<Client> clientall = organizationService.findAll();
//        model.addAttribute("clientall", clientall);
//        notifyService.addInfoMessage("Delete successful");
//        return "clients/index_all";
//    }
//    @RequestMapping(value ="/clients/edit/{id}", method = RequestMethod.GET)
//    public String editPage(CreateClientForm createClientForm, @PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Client client = organizationService.findById(id);
//        if (client == null) {
//            notifyService.addErrorMessage("Cannot find client #" + id);
//            return "redirect:/";
//        }
//        model.addAttribute("client", client);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String birthdt = df.format(client.getBirthdt());
//        model.addAttribute("birthdt", birthdt);
//        return "clients/edit";
//    }
//    @RequestMapping(value ="/clients/edit/{id}", method = RequestMethod.POST)
//    public String edit(@Valid CreateClientForm createClientForm, BindingResult bindingResult, @PathVariable("id") Long id, Model model) throws ParseException {
//        Client client = organizationService.findById(id);
//        if (client == null) {
//            notifyService.addErrorMessage("Cannot find client #" + id);
//            return "redirect:/";
//        }
//        if (bindingResult.hasErrors()) {
//            notifyService.addErrorMessage("Please fill the form correctly!");
//            model.addAttribute("client", client);
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            String birthdt = df.format(client.getBirthdt());
//            model.addAttribute("birthdt", birthdt);
//            return "/clients/edit";
//        }
//        String date_str = createClientForm.getBirthdt();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date birthDate = df.parse(date_str);
//        String fullName = createClientForm.getFullName();
//        client.setBirthdt(birthDate);
//        client.setFullName(fullName);
//        client.setSex(createClientForm.getSex());
//        client.setVat(createClientForm.getVat());
//        organizationService.edit(client);
//        notifyService.addInfoMessage("Edit successful");
//        return "redirect:/clients";
//    }
    @GetMapping(value = "/organizations/create")
    public String createPage(@ModelAttribute(name = "organizationModel") OrganizationModel organizationModel) {
        return "prsnorg/org/create-organization";
    }

    @PostMapping(value = "/organizations/create")
    public String create(@Valid OrganizationModel organizationModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/org/create-organization";
        }
        Organization organization = DTOConvertUtil.convert(organizationModel, Organization.class);
        this.organizationService.create(organization);
        notifyService.addInfoMessage("Organization with Id: " + organization.getId() + " was created.");
        return "redirect:/";
    }
}
