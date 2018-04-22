package com.insurance.ins.technical.controllers;

import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.models.*;
import com.insurance.ins.technical.services.UserService;
import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.utils.annotations.Log;
import com.insurance.ins.utils.notifications.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

import static com.insurance.ins.technical.store.WebConstants.*;

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
    public ModelAndView login(@RequestParam(required = false, name = "error") String error) {

        if (error != null && error.equals("true")) {
            notifyService.addErrorMessage(USER_NOT_EXIST_OR_WRONG_PASSWORD);
        }
        return new ModelAndView("users/login");
    }

    @PostMapping("/login")
    public ModelAndView loginConfirm(ModelAndView modelAndView) {

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
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "users/register";
        }

        if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            notifyService.addErrorMessage(PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME);
            return "users/register";
        }
        User user = userService.register(userModel);
        notifyService.addInfoMessage(String.format(USER_WITH_ID_S_WAS_CREATED,user.getId()));
        return "redirect:/login";
    }


    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String view_all(@ModelAttribute(name = "searchUserModel") SearchUserModel searchUserModel, Model model, @PageableDefault(size = 10) Pageable pageable) {
        AllUsersViewModel users = userService.searchUser(searchUserModel, pageable);
        if (
                (!searchUserModel.getCriteria().equals("") || !(searchUserModel.getCriteria() == null))
                        && !users.getUsers().hasContent()) {
            notifyService.addWarningMessage(CANNOT_FIND_USERS_WITH_GIVEN_SEARCH_CRITERIA);
        }
        model.addAttribute("users", users);
        return "users/show-users";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users/edit/{id}")
    public String editPage(@ModelAttribute(name = "editUserModel") EditUserModel editUserModel, @PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            notifyService.addErrorMessage(CANNOT_FIND_USER + id);
            return "redirect:/";
        }
        editUserModel = DTOConvertUtil.convert(user, EditUserModel.class);
        model.addAttribute("editUserModel", editUserModel);
        return "users/edit";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users/edit/{id}")
    @Log
    public String edit(@Valid EditUserModel editUserModel, BindingResult bindingResult, @PathVariable("id") Long id, Model model, @RequestParam(value = "action", required = true) String action) throws ParseException {
        if (action.equals("return")) {
            return "redirect:/users";
        }
        User userOld = userService.findById(id);
        if (userOld == null) {
            notifyService.addErrorMessage(CANNOT_FIND_USER + id);
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage(PLEASE_FILL_THE_FORM_CORRECTLY);
            return "users/edit";
        }
        if (!editUserModel.getPassword().equals(editUserModel.getConfirmPassword())) {
            notifyService.addErrorMessage(PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME);
            return "users/edit";
        }
        this.userService.edit(userOld,editUserModel);
        notifyService.addInfoMessage(EDIT_SUCCESSFUL);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/delete/{id}")
    @Log
    public String delete(@PathVariable("id") Long id, Model model) {
        User user = this.userService.findById(id);
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = loggedUser.getUsername();
        String profile = user.getProfile();
        if(profile.equals("ADMIN")){
            notifyService.addErrorMessage(CAN_T_DELETE_USER_WITH_PROFILE_ADMINISTRATOR);
            return "redirect:/users";
        }
        if(user.getUsername().equals(username)){
            notifyService.addErrorMessage(CAN_T_DELETE_YOUR_OWN_USER);
            return "redirect:/users";
        }
        try {
            this.userService.delete(id);
        } catch (Exception e) {
            notifyService.addErrorMessage(CAN_T_DELETE_THIS_USER_IT_IS_DISTRIBUTOR);
            return "redirect:/users";
        }
        notifyService.addInfoMessage(DELETE_SUCCESSFUL);
        return "redirect:/users";
    }
    @GetMapping("/users/log")
    @PreAuthorize("hasRole('ADMIN')")
    public String logUser(@ModelAttribute(name = "searchUserModel") SearchUserModel searchUserModel, Model model) {
        String searchBy = searchUserModel.getSearchBy();
        String criteria =  searchUserModel.getCriteria();
        if(!criteria.equals("")){
            List<UserLogsModel> userLogsModel = userService.searchUserLog(searchBy,criteria);
            if (userLogsModel.size()==0) {
                notifyService.addWarningMessage(CANNOT_FIND_LOGS_WITH_GIVEN_SEARCH_CRITERIA);
            }
            model.addAttribute("logs", userLogsModel);
        }
        return "users/logs-users";
    }


}
