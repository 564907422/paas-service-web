package com.paas.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CheckController {

    @ResponseBody
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String service() {
        return "success";
    }
}
