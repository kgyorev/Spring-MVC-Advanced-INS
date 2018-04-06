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
import java.text.ParseException;
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

    @RequestMapping(value ="/persons/delete/{id}", method = RequestMethod.GET)
    public String delete(SearchPersonModel searchPersonModel, @PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        Set<Contract> contracts = person.getContracts();
        int contracts_count = contracts.size();
        if (contracts_count != 0) {
            notifyService.addErrorMessage("Cannot delete client #" + id + " ,it has contracts.");
            return "redirect:/persons";
//            List<Person> personAll = personService.findAll();
//            model.addAttribute("personAll", personAll);
//            return "prsnorg/prsn/search-person";
        }
       personService.deleteById(id);
//        List<Person> personall = personService.findAll();
//        model.addAttribute("personall", personall);
        notifyService.addInfoMessage("Delete successful");
        return "redirect:/persons";
    }
    @RequestMapping(value ="/persons/edit/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value ="/persons/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(name = "personModel") EditPersonModel personModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model) throws ParseException {
        Person person = personService.findById(id);
        if (person == null) {
            notifyService.addErrorMessage("Cannot find person #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/prsn/edit-person";
        }
        Person personEdit = DTOConvertUtil.convert(personModel, Person.class);
        personService.edit(personEdit);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/persons";
    }
    @GetMapping(value = "/persons/create")
    public String createPage(@ModelAttribute(name = "personModel") PersonModel personModel) {
        return "prsnorg/prsn/create-person";
    }

    @PostMapping(value = "/persons/create")
    public String create(@Valid PersonModel personModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "/prsnorg/prsn/create-person";
        }
        Person person = DTOConvertUtil.convert(personModel, Person.class);
        this.personService.create(person);
        notifyService.addInfoMessage("Person with Id: " + person.getId() + " was created.");
        return "redirect:/";
    }
}
