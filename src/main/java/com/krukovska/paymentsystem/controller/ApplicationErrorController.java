package com.krukovska.paymentsystem.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.krukovska.paymentsystem.util.Constants.ERROR_LABEL;

@Controller
public class ApplicationErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            var statusCode = Integer.parseInt(status.toString());
            model.addAttribute("message",  HttpStatus.valueOf(statusCode));
        }
        return ERROR_LABEL;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
