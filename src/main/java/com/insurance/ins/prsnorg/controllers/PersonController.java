package com.insurance.ins.prsnorg.controllers;

import com.insurance.ins.prsnorg.entites.prsn.entities.Person;
import com.insurance.ins.prsnorg.entites.prsn.models.AllPersonsViewModel;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


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

    //    @RequestMapping("/clients/view/{id}")
//    public String view(@PathVariable("id") Long id, Model model) {
//        Person person = personService.findById(id);
//        if (person == null) {
//            notifyService.addErrorMessage("Cannot find person #" + id);
//            return "redirect:/";
//        }
//        model.addAttribute("person", person);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String birthdt = df.format(person.getBirthdt());
//        model.addAttribute("birthdt", birthdt);
//        Date now = new Date();
//        int age=person.getAge(now);
//        model.addAttribute("age", age);
//        Set<Contract> contractall = person.getContracts();
//        model.addAttribute("contractall", contractall);
//        return "clients/index";
//    }
    @GetMapping(value = "/persons")
    public String view_all(@ModelAttribute(name = "searchPersonModel") SearchPersonModel searchPersonModel, Model model, @PageableDefault(size = 10) Pageable pageable) {

//        model.addAttribute("clientall", personAll);
//        model.addAttribute("egn", "");
//        return "prsnorg/prsn/search-person";
        String egn = searchPersonModel.getEgn();
        String fullName = searchPersonModel.getFullName();

        AllPersonsViewModel personAll;

        if (!egn.equals("") && !fullName.equals("")) {
            personAll = personService.findAllByEgnAndFullName(egn, fullName, pageable);
        } else if (!egn.equals("") && fullName.equals("")) {
            personAll = personService.findAllByEgn(egn, pageable);
        } else if (egn.equals("")&& !fullName.equals("")) {
            personAll = personService.findAllByFullName(fullName, pageable);
        } else {
            personAll = personService.findAllByPage(pageable);
        }



//        if (egn != null && !egn.equals("") && fullName != null && !fullName.equals("")) {
//            personAll = personService.findAllByEgnAndFullName(egn, fullName, pageable);
//        } else if (egn != null && !egn.equals("") && (fullName == null || fullName.equals(""))) {
//            personAll = personService.findAllByEgn(egn, pageable);
//        } else if ((egn == null || egn.equals("") )&& (fullName != null && !fullName.equals(""))) {
//            personAll = personService.findAllByFullName(fullName, pageable);
//        } else {
//            personAll = personService.findAllByPage(pageable);
//        }

//        List<Person> allclients = personService.findAll();
//        List<Person> clientall;
//        if(egn!=null&&!egn.equals("")) {
//            Stream<Person> userstream = personAll.getCompanies().stream().filter(u -> u.getEgn().equals(egn));
//            Page<Person> clientall_tmp = userstream.collect(Collectors.toPage());
//            personAll.setCompanies(clientall_tmp);
//            if(clientall_tmp.size()==0)
//            {
//                notifyService.addWarningMessage("Cannot find client with EGN: " + egn);
//                clientall= allclients;
//            }
//            else
//                clientall = clientall_tmp;
//        }
//        else
//            clientall= allclients;
        model.addAttribute("clientall", personAll);
        return "prsnorg/prsn/search-person";
    }

    //    @PostMapping(value="/persons")
//    public String searchClient(SearchPersonModel searchPersonModel, Model model) {
//        String egn = searchPersonModel.getEgn();
//        List<Person> allclients = personService.findAll();
//        List<Person> clientall;
//        if(!egn.equals("")) {
//            Stream<Person> userstream = allclients.stream().filter(u -> u.getEgn().equals(egn));
//            List<Person> clientall_tmp = userstream.collect(Collectors.toList());
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
//        return "prsnorg/prsn/search-person";
//    }
//    @RequestMapping(value ="/clients/delete/{id}", method = RequestMethod.GET)
//    public String delete(SearchClientForm searchClientForm, @PathVariable("id") Long id, Model model) {
//        Object user = httpSession.getAttribute(USER_LOGIN);
//        if (user == null) {
//            notifyService.addErrorMessage("Please Login!");
//            return "redirect:/";
//        }
//        Client client = personService.findById(id);
//        if (client == null) {
//            notifyService.addErrorMessage("Cannot find client #" + id);
//            return "redirect:/";
//        }
//        Set<Contract> contracts = client.getContracts();
//        int contracts_count = contracts.size();
//        if (contracts_count != 0) {
//            notifyService.addErrorMessage("Cannot delete client #" + id + " ,it has contracts.");
//            List<Client> clientall = personService.findAll();
//            model.addAttribute("clientall", clientall);
//            return "clients/index_all";
//        }
//       personService.deleteById(id);
//        List<Client> clientall = personService.findAll();
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
//        Client client = personService.findById(id);
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
//        Client client = personService.findById(id);
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
//        client.setEgn(createClientForm.getEgn());
//        personService.edit(client);
//        notifyService.addInfoMessage("Edit successful");
//        return "redirect:/clients";
//    }
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
