package com.insurance.ins.technical.controllers;

import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.models.AllUsersViewModel;
import com.insurance.ins.technical.models.EditUserModel;
import com.insurance.ins.technical.models.SearchUserModel;
import com.insurance.ins.technical.models.UserModel;
import com.insurance.ins.technical.services.UserService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * Created by K on 15.3.2018 г..
 */
@Controller
public class AccountController {

    private final UserService userService;
    private final NotificationService notifyService;

    @Autowired
    public AccountController(UserService userService, NotificationService notifyService) {
        this.userService = userService;
        this.notifyService = notifyService;
    }




    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(@RequestParam(required=false,name="error") String error){

        if(error!=null&&error.equals("true")){
            notifyService.addErrorMessage("User not exist, or wrong password!");
        }
        return new ModelAndView("users/login");
    }
    @PostMapping("/login")
    public ModelAndView loginConfirm(ModelAndView modelAndView){

        return new ModelAndView("redirect:/");
    }





    @GetMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String createPage(@ModelAttribute(name = "userModel") UserModel userModel) {
        return "users/register";
    }

    @PostMapping(value = "/register")
    public String create(@Valid UserModel userModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "users/register";
        }

        if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            notifyService.addErrorMessage("Password and ConfirmPassword are not the same!");
            return "users/register";
        }
        User user = userService.register(userModel);
        notifyService.addInfoMessage("User with Id: " + user.getId() + " was created.");
        return "redirect:/login";
    }



    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String view_all(@ModelAttribute(name = "searchUserModel") SearchUserModel searchUserModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllUsersViewModel users = userService.searchUser(searchUserModel, pageable);
        if (
                (!searchUserModel.getCriteria().equals("") || !(searchUserModel.getCriteria() == null))
                        && !users.getUsers().hasContent()) {
            notifyService.addWarningMessage("Cannot find users with given search criteria.");
        }
        model.addAttribute("users", users);
        return "users/show-users";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/edit/{id}", method = RequestMethod.GET)
    public String editPage(@ModelAttribute(name = "editUserModel") EditUserModel editUserModel, @PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            notifyService.addErrorMessage("Cannot find user #" + id);
            return "redirect:/";
        }
        editUserModel = DTOConvertUtil.convert(user, EditUserModel.class);
        model.addAttribute("editUserModel", editUserModel);
        return "users/edit";
    }
    @RequestMapping(value = "/users/edit/{id}", method = RequestMethod.POST)
    public String edit(@Valid EditUserModel editUserModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "redirect:/users";
        }
        if (editUserModel == null) {
            notifyService.addErrorMessage("Cannot find user #" + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "users/edit";
        }
        if (!editUserModel.getPassword().equals(editUserModel.getConfirmPassword())) {
            notifyService.addErrorMessage("Password and ConfirmPassword are not the same!");
            return "users/edit";
        }
        this.userService.edit(editUserModel);
        notifyService.addInfoMessage("Edit successful");
        return "redirect:/users";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model){
        try {
            this.userService.delete(id);
        } catch (Exception e){
            notifyService.addErrorMessage("Can't Delete this User,it has Distributor!");
            return "redirect:/users";
        }
        notifyService.addInfoMessage("Delete successful");
        return "redirect:/users";
    }
}
