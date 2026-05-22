package com.fdjloto.api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    // @RequestMapping("/error")
    // @RequestMapping(value = "/error", method = RequestMethod.GET)
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "forward:/errors/401.html";
            }

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "forward:/errors/403.html";
            }

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "forward:/errors/404.html";
            }

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "forward:/errors/500.html";
            }
        }

        return "forward:/errors/500.html";
    }
}
