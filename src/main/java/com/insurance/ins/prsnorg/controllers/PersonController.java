package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.prsnorg.entites.prsn.models.EditPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.PersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.SearchPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.services.PersonService;
import com.insurance.ins.technical.store.WebConstants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.insurance.ins.technical.store.WebConstants.*;


@Controller
public class PersonController {



    private final PersonService personService;
    private final NotificationService notifyService;

    @Autowired
    public PersonController(NotificationService notifyService, PersonService personService) {
        this.notifyService = notifyService;
        this.personService = personService;
    }
    @GetMapping(value = "/persons")
    @PreAuthorize("hasRole('USER')")
    public String view_all(@ModelAttribute(name = "searchPersonModel") SearchPersonModel searchPersonModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllPersonsViewModel personAll =  personService.searchPerson(searchPersonModel,pageable);
        if(
                (!searchPersonModel.getEgn().equals("")||!searchPersonModel.getFullName().equals(""))
                        &&!personAll.getPersons().hasContent()) {
                notifyService.addWarningMessage(CANNOT_FIND_CLIENTS_WITH_GIVEN_SEARCH_CRITERIA);
            }
        model.addAttribute("personAll", personAll);
        return "prsnorg/prsn/search-person";
    }

    @GetMapping(value ="/persons/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String delete(SearchPersonModel searchPersonModel, @PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage(CANNOT_FIND_PERSON + id);
            return "redirect:/";
        }
        Set<Contract> contracts = person.getContracts();
        int contracts_count = contracts.size();
        if (contracts_count != 0) {
            notifyService.addErrorMessage(String.format(CANNOT_DELETE_PERSON_S_IT_HAS_CONTRACTS,id));
            return "redirect:/persons";
        }
        personService.deleteById(id);
        notifyService.addInfoMessage(DELETE_SUCCESSFUL);
        return "redirect:/persons";
    }
    @GetMapping(value ="/persons/{id}")
    @PreAuthorize("hasRole('USER')")
    public String viewPerson(@ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id, Model model) {

        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage(CANNOT_FIND_PERSON + id);
            return "redirect:/";
        }
        personModel = DTOConvertUtil.convert(person, EditPersonModel.class);
        model.addAttribute("personModel", personModel);
        return "prsnorg/prsn/view-person";
    }
    @GetMapping(value ="/persons/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String editPage(@ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage(CANNOT_FIND_PERSON + id);
            return "redirect:/";
        }
        personModel = DTOConvertUtil.convert(person, EditPersonModel.class);
        model.addAttribute("personModel", personModel);
        return "prsnorg/prsn/edit-person";
    }
    @GetMapping(value ="/persons/confirm/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public String confirmEditPage(@Valid @ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id,BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/persons";
        }
        if (personModel == null) {
            notifyService.addErrorMessage(CANNOT_FIND_PERSON + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/prsn/edit-person";
        }
        return "prsnorg/prsn/confirm-edit-person";
    }
    @PostMapping(value ="/persons/edit/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    @Log
    public String edit(@Valid @ModelAttribute(name = "personModel") EditPersonModel personModel, BindingResult bindingResult, @PathVariable("id") Long id,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "prsnorg/prsn/edit-person";
        }
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage(WebConstants.CANNOT_FIND_PERSON + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/prsn/edit-person";
        }
        personService.edit(person,personModel);
        notifyService.addInfoMessage(EDIT_SUCCESSFUL);
        return "redirect:/persons";
    }
    @GetMapping(value = "/persons/create")
    @PreAuthorize("hasRole('USER')")
    public String createPage(@ModelAttribute(name = "personModel") PersonModel personModel) {
        return "prsnorg/prsn/create-person";
    }
    @GetMapping(value = "/persons/confirm/create")
    @PreAuthorize("hasRole('USER')")
    public String confirmPage(@Valid @ModelAttribute(name = "personModel") PersonModel personModel,BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/prsn/create-person";
        }
        return "prsnorg/prsn/confirm-create-person";
    }

    @PostMapping(value = "/persons/create")
    @PreAuthorize("hasRole('USER')")
    @Log
    public String create(@Valid PersonModel personModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "prsnorg/prsn/create-person";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "prsnorg/prsn/create-person";
        }
        Person person = DTOConvertUtil.convert(personModel, Person.class);
        this.personService.create(person);
        notifyService.addInfoMessage(String.format(PERSON_WITH_ID_S_WAS_CREATED,person.getId()));
        return "redirect:/";
    }
    @GetMapping(value="/rest/persons",produces = "application/json")
    @ResponseBody
    @CrossOrigin
    public List<PersonModel> allPersons(@RequestParam(value="searchPersonCriteria" ,required = false) String criteria) {
        List<Person> persons = this.personService.findAllByIdOrFullNameContainsOrEgnContains(criteria);
        List<PersonModel> personModel=new ArrayList<>();
        for (Person person : persons) {
            PersonModel personModeled = DTOConvertUtil.convert(person, PersonModel.class);
            personModel.add(personModeled);
        }
        return personModel;
    }
}
