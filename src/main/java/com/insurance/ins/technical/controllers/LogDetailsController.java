package com.insurance.ins.technical.controllers;

import com.insurance.ins.technical.entites.LogDetails;
import com.insurance.ins.technical.models.LogDetailsModel;
import com.insurance.ins.technical.services.LogDetailsService;
import com.insurance.ins.utils.DTOConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LogDetailsController {

    private final LogDetailsService logDetailsService;


    @Autowired
    public LogDetailsController(LogDetailsService logDetailsService) {

        this.logDetailsService = logDetailsService;
    }

    @GetMapping("/logs/all")
    @PreAuthorize("hasRole('ADMIN')")
    public String add(Model model) {
        List<LogDetails> allLogs = this.logDetailsService.findAll();
        List<LogDetailsModel> allLogsView = DTOConvertUtil.convert(allLogs, LogDetailsModel.class);
        model.addAttribute("viewLogs", allLogsView);
        return "users/logs/view";
    }

    @GetMapping("/logs/search")
    @PreAuthorize("hasRole('ADMIN')")
    public String search(@RequestParam String name, Model model) {
        List<LogDetails> allLogs = this.logDetailsService.findByNameContains(name);
        List<LogDetailsModel> allLogsView = DTOConvertUtil.convert(allLogs, LogDetailsModel.class);
        model.addAttribute("viewLogs", allLogsView);
        return "users/logs/view";
    }
    @GetMapping("/logs/clear")
    @PreAuthorize("hasRole('ADMIN')")
    public String search(Model model) {
        this.logDetailsService.deleteAllLogs();
        model.addAttribute("viewLogs", new ArrayList<>());
        return "users/logs/view";
    }

}
