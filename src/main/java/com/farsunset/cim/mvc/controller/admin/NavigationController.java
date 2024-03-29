package com.farsunset.cim.mvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NavigationController {

    @ResponseBody
    @GetMapping(value = "/")
    public String index() {
        return "cim";
    }

    //@GetMapping(value = "/")
    public ModelAndView index(ModelAndView model) {
        model.setViewName("console/index");
        return model;
    }

    @GetMapping(value = "/webclient")
    public ModelAndView webclient(ModelAndView model) {
        model.setViewName("console/webclient/index");
        return model;
    }

}
