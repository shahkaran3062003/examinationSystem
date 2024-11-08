package com.roima.examinationSystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testing {

    @GetMapping("/test")
    public String test(){
        return "Hello, World!!!";
    }
}
