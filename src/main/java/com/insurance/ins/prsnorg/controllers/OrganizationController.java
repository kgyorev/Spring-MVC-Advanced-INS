package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.models.AllOrganizationsViewModel;
import com.insurance.ins.prsnorg.entites.org.models.EditOrganizationModel;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;


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

    @RequestMapping(value ="/organizations/delete/{id}", method = RequestMethod.GET)
    public String delete(SearchOrganizationModel searchOrganizationModel, @PathVariable("id") Long id, Model model) {
        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage("Cannot find organization #" + id);
            return "redirect:/";
        }
        Set<Contract> contracts = organization.getContracts();
        int contracts_count = contracts.size();
        if (contracts_count != 0) {
            notifyService.addErrorMessage("Cannot delete organization #" + id + " ,it has contracts.");
            return "redirect:/organizations";
        }
        organizationService.deleteById(id);
        notifyService.addInfoMessage("Delete successful");
        return "redirect:/organizations";
    }
    @RequestMapping(value ="/organizations/{id}", method = RequestMethod.GET)
    public String viewOrganization(@ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, @PathVariable("id") Long id, Model model) {

        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage("Cannot find organization #" + id);
            return "redirect:/";
        }
        organizationModel = DTOConvertUtil.convert(organization, EditOrganizationModel.class);
        model.addAttribute("organizationModel", organizationModel);
        return "prsnorg/org/view-organization";
    }
    @RequestMapping(value ="/organizations/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, @PathVariable("id") Long id, Model model) {

        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage("Cannot find organization #" + id);
            return "redirect:/";
        }
        organizationModel = DTOConvertUtil.convert(organization, EditOrganizationModel.class);
        model.addAttribute("organizationModel", organizationModel);
        return "prsnorg/org/edit-organization";
    }
    @RequestMapping(value ="/organizations/confirm/edit/{id}", method = RequestMethod.GET)
    public String editConfirm(@Valid @ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam(value="action", required=true) String action){
        if(action.equals("return")){
            return "redirect:/organizations";
        }
        if (organizationModel == null) {
            notifyService.addErrorMessage("Cannot find organization #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/org/edit-organization";
        }
        return "prsnorg/org/confirm-edit-organization";
    }
    @RequestMapping(value ="/organizations/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam(value="action", required=true) String action){
        if(action.equals("return")){
            return "/prsnorg/org/edit-organization";
        }

        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage("Cannot find organization #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/org/edit-organization";
        }
        Organization organizationEdit = DTOConvertUtil.convert(organizationModel, Organization.class);
        organizationService.edit(organizationEdit);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/organizations";
    }
    @GetMapping(value = "/organizations/create")
    public String createPage(@ModelAttribute(name = "organizationModel") OrganizationModel organizationModel) {
        return "prsnorg/org/create-organization";
    }
    @GetMapping(value = "/organizations/confirm/create")
    public String confirmCreate(@Valid @ModelAttribute(name = "organizationModel") OrganizationModel organizationModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/org/create-organization";
        }
        return "/prsnorg/org/confirm-create-organization";
    }

    @PostMapping(value = "/organizations/create")
    public String create(@Valid OrganizationModel organizationModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "/prsnorg/org/create-organization";
        }
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