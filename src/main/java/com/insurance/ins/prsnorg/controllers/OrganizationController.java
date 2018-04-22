package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.models.AllOrganizationsViewModel;
import com.insurance.ins.prsnorg.entites.org.models.EditOrganizationModel;
import com.insurance.ins.prsnorg.entites.org.models.OrganizationModel;
import com.insurance.ins.prsnorg.entites.org.models.SearchOrganizationModel;
import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.annotations.Log;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

import static com.insurance.ins.technical.store.WebConstants.*;


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
    @PreAuthorize("hasRole('USER')")
    public String view_all(@ModelAttribute(name = "searchOrganizationModel") SearchOrganizationModel searchOrganizationModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllOrganizationsViewModel organizationAll =  organizationService.searchOrganization(searchOrganizationModel,pageable);

        if((!searchOrganizationModel.getVat().equals("")||!searchOrganizationModel.getFullName().equals(""))
                &&!organizationAll.getOrganizations().hasContent()) {
            notifyService.addWarningMessage(CANNOT_FIND_CLIENTS_WITH_GIVEN_SEARCH_CRITERIA);
        }
        model.addAttribute("organizationAll", organizationAll);
        return "prsnorg/org/search-organization";
    }

    @GetMapping(value ="/organizations/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String delete(SearchOrganizationModel searchOrganizationModel, @PathVariable("id") Long id, Model model) {
        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage(CANNOT_FIND_ORGANIZATION + id);
            return "redirect:/";
        }
        Set<Contract> contracts = organization.getContracts();
        int contracts_count = contracts.size();
        if (contracts_count != 0) {
            notifyService.addErrorMessage(String.format(CANNOT_DELETE_ORGANIZATION_S_IT_HAS_CONTRACTS,id));
            return "redirect:/organizations";
        }
        organizationService.deleteById(id);
        notifyService.addInfoMessage(DELETE_SUCCESSFUL);
        return "redirect:/organizations";
    }
    @GetMapping(value ="/organizations/{id}")
    @PreAuthorize("hasRole('USER')")
    public String viewOrganization(@ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, @PathVariable("id") Long id, Model model) {

        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage(CANNOT_FIND_ORGANIZATION + id);
            return "redirect:/";
        }
        organizationModel = DTOConvertUtil.convert(organization, EditOrganizationModel.class);
        model.addAttribute("organizationModel", organizationModel);
        return "prsnorg/org/view-organization";
    }
    @GetMapping(value ="/organizations/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String editPage(@ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel,@RequestParam(required=false,name="secondaryActivity") String secondaryActivity, @PathVariable("id") Long id, Model model) {
        httpSession.setAttribute("secondaryActivity",secondaryActivity);
        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage(CANNOT_FIND_ORGANIZATION + id);
            return "redirect:/";
        }
        organizationModel = DTOConvertUtil.convert(organization, EditOrganizationModel.class);
        model.addAttribute("organizationModel", organizationModel);
        return "prsnorg/org/edit-organization";
    }
    @GetMapping(value ="/organizations/confirm/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String editConfirm(@Valid @ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam(value="action", required=true) String action){
        if(action.equals("return")){
            return "redirect:/organizations";
        }
        if (organizationModel == null) {
            notifyService.addErrorMessage(CANNOT_FIND_ORGANIZATION + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/org/edit-organization";
        }
        return "prsnorg/org/confirm-edit-organization";
    }
    @PostMapping(value ="/organizations/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String edit(@Valid @ModelAttribute(name = "organizationModel") EditOrganizationModel organizationModel, BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam(value="action", required=true) String action){
        if(action.equals("return")){
            return "prsnorg/org/edit-organization";
        }

        Organization organization = organizationService.findById(id);
        if (organization == null) {
            notifyService.addErrorMessage(CANNOT_FIND_ORGANIZATION + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/org/edit-organization";
        }
        organizationService.edit(organization,organizationModel);
        notifyService.addInfoMessage(EDIT_SUCCESSFUL);
        Object mainActivity = httpSession.getAttribute("mainActivity");
        Object secondaryActivity = httpSession.getAttribute("secondaryActivity");
        if(secondaryActivity!=null&&secondaryActivity.equals("true")){
            httpSession.setAttribute("secondaryActivity",null);
            httpSession.setAttribute("mainActivity",null);
            return "redirect:/"+mainActivity;
        }
        return "redirect:/organizations";
    }
    @GetMapping(value = "/organizations/create")
    @PreAuthorize("hasRole('USER')")
    public String createPage(@ModelAttribute(name = "organizationModel") OrganizationModel organizationModel) {
        return "prsnorg/org/create-organization";
    }
    @GetMapping(value = "/organizations/confirm/create")
    public String confirmCreate(@Valid @ModelAttribute(name = "organizationModel") OrganizationModel organizationModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/org/create-organization";
        }
        return "prsnorg/org/confirm-create-organization";
    }

    @PostMapping(value = "/organizations/create")
    @PreAuthorize("hasRole('USER')")
    @Log
    public String create(@Valid OrganizationModel organizationModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "prsnorg/org/create-organization";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/org/create-organization";
        }
        Organization organization = DTOConvertUtil.convert(organizationModel, Organization.class);
        this.organizationService.create(organization);
        notifyService.addInfoMessage(String.format(ORGANIZATION_WITH_ID_S_WAS_CREATED,organization.getId()));
        return "redirect:/";
    }
}
