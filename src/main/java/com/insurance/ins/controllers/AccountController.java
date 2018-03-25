package com.insurance.ins.controllers;

import com.insurance.ins.utils.DTOConvertUtil;
import com.insurance.ins.models.binding.UserEditBindingModel;
import com.insurance.ins.models.binding.UserRegisterBindingModel;
import com.insurance.ins.technical.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by K on 15.3.2018 г..
 */
@Controller
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }




    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(){

        return new ModelAndView("users/login");
    }
    @PostMapping("/login")
    public ModelAndView loginConfirm(ModelAndView modelAndView){

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(){


        return new ModelAndView("users/register");

    }
    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel, ModelAndView modelAndView ){
         modelAndView.setViewName("users/register");
        if (userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {

            userService.register(userRegisterBindingModel);
            modelAndView.setViewName("redirect:/login");
        }
        return modelAndView;

    }
    @GetMapping("/users/show")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showUsers(ModelAndView modelAndView){
        modelAndView.setViewName("users/show-users");
        modelAndView.addObject("users",this.userService.getAllUsers());
        return modelAndView;

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelAndView modelAndView){
        modelAndView.setViewName("users/edit");

        UserEditBindingModel user = DTOConvertUtil.convert(this.userService.findById(id), UserEditBindingModel.class);
        modelAndView.addObject("user",user);
        return modelAndView;
    }
    @PostMapping("/users/edit/{id}")
    public String editConfirm(@Valid @ModelAttribute(name = "user") UserEditBindingModel userEditBindingModel,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("user",userEditBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.model",bindingResult);
            return  "redirect:/user/edit/"+id;
        } else{

            this.userService.edit(userEditBindingModel);
            return "redirect:/users/show";
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model){

        this.userService.delete(id);
        return "redirect:/users/show";
    }
}
