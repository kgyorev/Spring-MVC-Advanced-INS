package com.insurance.ins.technical.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.insurance.ins.technical.store.WebConstants.UNAUTHORIZED;

/**
 * Created by K on 21.3.2018 г..
 */
@Controller
public class AccessController {



    @GetMapping("/unauthorized")
    public String unauthorized(){
        return UNAUTHORIZED;
    }

}
