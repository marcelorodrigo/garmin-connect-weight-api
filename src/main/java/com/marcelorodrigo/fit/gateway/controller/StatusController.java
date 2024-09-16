package com.marcelorodrigo.fit.gateway.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Hidden
@RestController("/")
public class StatusController {

    @GetMapping
    public void status(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

}
