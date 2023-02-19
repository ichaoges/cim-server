package com.farsunset.cim.mvc.controller.admin;

import com.farsunset.cim.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/console/session")
public class SessionController {

    @Resource
    private SessionService sessionService;

    //@GetMapping(value = "/list")
    public String list(Model model) {
        model.addAttribute("sessionList", sessionService.findAll());
        return "console/session/manage";
    }

}
