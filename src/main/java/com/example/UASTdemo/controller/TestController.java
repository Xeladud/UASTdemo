package com.example.UASTdemo.controller;

import com.example.UASTdemo.service.ASTGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TestController {
    @GetMapping("/test")
    public @ResponseBody String test() throws IOException {
        ASTGenerator astGenerator = new ASTGenerator();
        astGenerator.main();
        return "test";
    }
}
