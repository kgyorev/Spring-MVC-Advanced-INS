package com.insurance.ins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by K on 7.3.2018 г..
 */
@Controller
public class HomeController {

    private final HttpSession httpSession;

    @Autowired
    public HomeController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @GetMapping("/")
    public String index(Model model){

        return "home/index";
    }
}
