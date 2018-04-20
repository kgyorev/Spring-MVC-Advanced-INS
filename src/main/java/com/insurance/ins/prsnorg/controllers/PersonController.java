package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.business.entites.Contract;
import com.insurance.ins.prsnorg.entites.prsn.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
import com.insurance.ins.prsnorg.entites.prsn.models.EditPersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.PersonModel;
import com.insurance.ins.prsnorg.entites.prsn.models.SearchPersonModel;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;


@Controller
public class PersonController {
    private final HttpSession httpSession;
    private final PersonService personService;
    private final NotificationService notifyService;

    @Autowired
    public PersonController(NotificationService notifyService, HttpSession httpSession, PersonService personService) {
        this.notifyService = notifyService;
        this.httpSession = httpSession;
        this.personService = personService;
    }
    @GetMapping(value = "/persons")
    public String view_all(@ModelAttribute(name = "searchPersonModel") SearchPersonModel searchPersonModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllPersonsViewModel personAll =  personService.searchPerson(searchPersonModel,pageable);
        if(
                (!searchPersonModel.getEgn().equals("")||!searchPersonModel.getFullName().equals(""))
                        &&!personAll.getPersons().hasContent()) {
                notifyService.addWarningMessage("Cannot find clients with given search criteria.");
            }
        model.addAttribute("personAll", personAll);
        return "prsnorg/prsn/search-person";
    }

    @GetMapping(value ="/persons/delete/{id}")
    public String delete(SearchPersonModel searchPersonModel, @PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        Set<Contract> contracts = person.getContracts();
        int contracts_count = contracts.size();
        if (contracts_count != 0) {
            notifyService.addErrorMessage("Cannot delete person #" + id + " ,it has contracts.");
            return "redirect:/persons";
        }
        personService.deleteById(id);
        notifyService.addInfoMessage("Delete successful");
        return "redirect:/persons";
    }
    @GetMapping(value ="/persons/{id}")
    public String viewPerson(@ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id, Model model) {

        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        personModel = DTOConvertUtil.convert(person, EditPersonModel.class);
        model.addAttribute("personModel", personModel);
        return "prsnorg/prsn/view-person";
    }
    @GetMapping(value ="/persons/edit/{id}")
    public String editPage(@ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        personModel = DTOConvertUtil.convert(person, EditPersonModel.class);
        model.addAttribute("personModel", personModel);
        return "prsnorg/prsn/edit-person";
    }
    @GetMapping(value ="/persons/confirm/edit/{id}")
    public String confirmEditPage(@Valid @ModelAttribute(name = "personModel") EditPersonModel personModel, @PathVariable("id") Long id,BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/persons";
        }
        if (personModel == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "prsnorg/prsn/edit-person";
        }
        return "prsnorg/prsn/confirm-edit-person";
    }
    @PostMapping(value ="/persons/edit/{id}")
    public String edit(@Valid @ModelAttribute(name = "personModel") EditPersonModel personModel, BindingResult bindingResult, @PathVariable("id") Long id,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "prsnorg/prsn/edit-person";
        }
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "prsnorg/prsn/edit-person";
        }
        personService.edit(person,personModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/persons";
    }
    @GetMapping(value = "/persons/create")
    public String createPage(@ModelAttribute(name = "personModel") PersonModel personModel) {
        return "prsnorg/prsn/create-person";
    }
    @GetMapping(value = "/persons/confirm/create")
    public String confirmPage(@Valid @ModelAttribute(name = "personModel") PersonModel personModel,BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "prsnorg/prsn/create-person";
        }
        return "prsnorg/prsn/confirm-create-person";
    }

    @PostMapping(value = "/persons/create")
    public String create(@Valid PersonModel personModel, BindingResult bindingResult,@RequestParam(value="action", required=true) String action) {
        if(action.equals("return")){
            return "prsnorg/prsn/create-person";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "prsnorg/prsn/create-person";
        }
        Person person = DTOConvertUtil.convert(personModel, Person.class);
        this.personService.create(person);
        notifyService.addInfoMessage("Person with Id: " + person.getId() + " was created.");
        return "redirect:/";
    }
}
