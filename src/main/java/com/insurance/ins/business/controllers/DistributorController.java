package com.insurance.ins.business.controllers;

import com.insurance.ins.business.entites.Distributor;
import com.insurance.ins.business.models.contract.AllContractsViewModel;
import com.insurance.ins.business.models.distributor.AllDistributorsViewModel;
import com.insurance.ins.business.models.distributor.DistributorModel;
import com.insurance.ins.business.models.distributor.SearchDistributorModel;
import com.insurance.ins.business.services.ContractService;
import com.insurance.ins.business.services.DistributorService;
import com.insurance.ins.prsnorg.entites.org.Organization;
import com.insurance.ins.prsnorg.entites.org.services.OrganizationService;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.services.UserService;
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

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.insurance.ins.technical.store.WebConstants.*;

@Controller
public class DistributorController {

    private final ContractService contractService;
    private final DistributorService distributorService;
    private final OrganizationService organizationService;
    private final UserService userService;

    private final NotificationService notifyService;

    @Autowired
    public DistributorController(ContractService contractService, DistributorService distributorService, OrganizationService organizationService, UserService userService, NotificationService notifyService) {
        this.contractService = contractService;
        this.distributorService = distributorService;
        this.organizationService = organizationService;
        this.userService = userService;
        this.notifyService = notifyService;
    }

    @GetMapping(value = "/distributors/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String view(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel, @PathVariable("id") Long id, Model model, @PageableDefault(size = 10) Pageable page) {
        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage(CANNOT_FIND_DISTRIBUTOR + id);
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

    @GetMapping(value = "/distributors")
    @PreAuthorize("hasRole('MODERATOR')")
    public String view_all(@ModelAttribute(name = "searchDistributorModel") SearchDistributorModel searchDistributorModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllDistributorsViewModel distributorall = distributorService.searchDistributor(searchDistributorModel, pageable);

        if (
                (!searchDistributorModel.getReferenceId().equals("") || !(searchDistributorModel.getReferenceId() == null))
                        && !distributorall.getDistributors().hasContent()) {
            notifyService.addWarningMessage(CANNOT_FIND_DISTRIBUTORS_WITH_GIVEN_SEARCH_CRITERIA);
        }
        model.addAttribute("distributorall", distributorall);
        return "business/distributor/search-distributor";
    }

    @GetMapping(value = "/distributors/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String editPage(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel, @PathVariable("id") Long id, Model model) {
        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage(CANNOT_FIND_DISTRIBUTOR + id);
            return "redirect:/";
        }
        distributorModel = DTOConvertUtil.convert(distributor, DistributorModel.class);
        model.addAttribute("distributorModel", distributorModel);
        return "business/distributor/edit-distributor";
    }

    @GetMapping(value = "/distributors/confirm/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String confirmEdit(@Valid @ModelAttribute(name = "distributorModel") DistributorModel distributorModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/distributors";
        }
        String organizationId = distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage(REFERENCE_ORGANIZATION_NOT_FOUND);
            return "business/distributor/edit-distributor";
        }

        String userId = distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage(REFERENCE_USER_NOT_FOUND);
            return "business/distributor/edit-distributor";
        }

        Distributor distributor = distributorService.findById(id);
        if (distributor == null) {
            notifyService.addErrorMessage(CANNOT_FIND_DISTRIBUTOR + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "business/distributor/edit-distributor";
        }
        return "business/distributor/confirm-edit-distributor";
    }

    @PostMapping(value = "/distributors/edit/{id}")
    @Log
    @PreAuthorize("hasRole('MODERATOR')")
    public String edit(@Valid DistributorModel distributorModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "business/distributor/edit-distributor";
        }
        if (distributorModel == null) {
            notifyService.addErrorMessage(CANNOT_FIND_DISTRIBUTOR + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "business/distributor/edit-distributor";
        }

        String organizationId = distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage(REFERENCE_ORGANIZATION_NOT_FOUND);
            return "business/distributor/edit-distributor";
        }

        String userId = distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage(REFERENCE_USER_NOT_FOUND);
            return "business/distributor/edit-distributor";
        }
        distributorService.edit(distributorModel);
        notifyService.addInfoMessage(EDIT_SUCCESSFUL);
        return "redirect:/distributors";
    }

    @GetMapping(value = "/distributors/create")
    @PreAuthorize("hasRole('MODERATOR')")
    public String createPage(@ModelAttribute(name = "distributorModel") DistributorModel distributorModel) {
        return "business/distributor/create-distributor";
    }


    @GetMapping(value = "/distributors/confirm/create")
    @PreAuthorize("hasRole('MODERATOR')")
    public String confirmCreate(@Valid @ModelAttribute(name = "distributorModel") DistributorModel distributorModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "business/distributor/create-distributor";
        }
        return "business/distributor/confirm-create-distributor";
    }


    @PostMapping(value = "/distributors/create")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String create(@Valid DistributorModel distributorModel, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("return")) {
            return "business/distributor/create-distributor";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "business/distributor/create-distributor";
        }
        Distributor distributor = DTOConvertUtil.convert(distributorModel, Distributor.class);


        String organizationId = distributorModel.getOrganization();
        Organization organization = organizationService.findById(Long.valueOf(organizationId));
        if (organization == null) {
            notifyService.addErrorMessage(REFERENCE_ORGANIZATION_NOT_FOUND);
            return "business/distributor/create-distributor";
        }

        String userId = distributorModel.getUser();
        User user = userService.findById(Long.valueOf(userId));
        if (user == null) {
            notifyService.addErrorMessage(REFERENCE_USER_NOT_FOUND);
            return "business/distributor/create-distributor";
        }


        distributor.setOrganization(organization);
        distributor.setUser(user);
        distributorService.create(distributor);
        notifyService.addInfoMessage(String.format(DISTRIBUTOR_WITH_ID_S_WAS_CREATED,distributor.getId()));
        return "redirect:/";
    }

    @GetMapping(value = "/rest/distributors", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    public List<DistributorModel> allProducts(@RequestParam(value = "searchDistributorCriteria", required = false) String criteria) {
        List<Distributor> distributors = this.distributorService.findAllByIdOrFullName(criteria);
        List<DistributorModel> distributorModel = new ArrayList<>();
        for (Distributor distributor : distributors) {
            DistributorModel distributorModeled = DTOConvertUtil.convert(distributor, DistributorModel.class);
            distributorModel.add(distributorModeled);
        }
        return distributorModel;
    }
}

