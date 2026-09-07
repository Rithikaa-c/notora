package com.rithikaa.notora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
    @GetMapping("/choose")
    public String choosePage() {
        return "choose";
    }


}
