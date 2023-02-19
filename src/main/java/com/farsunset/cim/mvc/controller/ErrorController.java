package com.farsunset.cim.mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ichaoge
 */
@RestController
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String index(HttpServletRequest request, Exception ex) {
        return "error";
    }
}
