package com.ohgiraffers.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String defaultLocation(){
        return "main";
    }

    @RequestMapping("/main")
    public void main(){}
}
