package com.quiz.quizapp.controller;

import com.quiz.quizapp.security.JwtService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coops")
public class CoopController {
    private final JwtService jwtService;

    public CoopController(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
