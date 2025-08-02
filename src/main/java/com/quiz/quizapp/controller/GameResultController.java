package com.quiz.quizapp.controller;

import com.quiz.quizapp.entity.GameResult;
import com.quiz.quizapp.entity.User;
import com.quiz.quizapp.repository.GameResultRepository;
import com.quiz.quizapp.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/game-results")
public class GameResultController {

    private final GameResultRepository repository;

    public GameResultController(GameResultRepository repository, UserDetailsServiceImpl userService) {
        this.repository = repository;
        this.userService = userService;
    }

    private final UserDetailsServiceImpl userService;

    @PostMapping
    public ResponseEntity<?> saveResult(@RequestBody GameResult result, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        result.setUser(user);
        result.setPlayedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(result));
    }

    @GetMapping("/me")
    public List<GameResult> getMyResults(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return repository.findByUser(user);
    }
}

