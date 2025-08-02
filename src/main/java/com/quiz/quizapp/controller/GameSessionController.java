package com.quiz.quizapp.controller;


import com.quiz.quizapp.entity.GameSession;
import com.quiz.quizapp.service.GameSessionService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/gamesessions")
public class GameSessionController {

    private final GameSessionService service;

    public GameSessionController(GameSessionService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public GameSession startNewSession(@RequestParam Long subjectId,
                                       @RequestParam Long modulId,
                                       @RequestParam(defaultValue = "10") int amount,
                                       Principal principal) {
        return service.createNewSession(principal.getName(), subjectId, modulId, amount);
    }

    @PostMapping("/{id}/answer")
    public void answer(@PathVariable Long id,
                       @RequestBody AnswerDto answer,
                       Principal principal) {
        service.answerQuestion(id, answer.questionId(), answer.selectedAnswerIndex(), principal.getName());
    }

    @GetMapping("/{id}")
    public GameSession getSession(@PathVariable Long id, Principal principal) {
        return service.getSession(id, principal.getName());
    }

    @GetMapping("/open")
    public List<GameSession> getOpen(Principal principal) {
        return service.getOpenSessions(principal.getName());
    }

    @PostMapping("/{id}/complete")
    public void complete(@PathVariable Long id, Principal principal) {
        service.markAsCompleted(id, principal.getName());
    }

    public record AnswerDto(Long questionId, int selectedAnswerIndex) {}
}

