package com.illiapinchuk.todoapp.controller;

import com.illiapinchuk.todoapp.security.GetPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String showWelcomePage(ModelMap model) {
        model.put("name", GetPrincipal.getLoggedUserName());
        return "home";
    }
}
