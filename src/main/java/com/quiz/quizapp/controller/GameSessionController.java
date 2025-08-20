package com.quiz.quizapp.controller;


import com.quiz.quizapp.dto.AnswerDto;
import com.quiz.quizapp.dto.GameSessionDto;
import com.quiz.quizapp.entity.GameSession;
import com.quiz.quizapp.security.JwtService;
import com.quiz.quizapp.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gamesessions")
public class GameSessionController {

    private final GameSessionService service;
    private final JwtService jwtService;

    public GameSessionController(GameSessionService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/start")
    public GameSessionDto startNewSession(@RequestParam Long subjectId,
                                          @RequestParam Long modulId,
                                          @RequestParam(defaultValue = "10") int amount,
                                          Principal principal) {
        GameSession session = service.createNewSession(principal.getName(), subjectId, modulId, amount);
        System.out.println("Fragen geladen: " + session.getQuestions());
        GameSessionDto dto = service.toDto(session);
        return dto;
    }


    @PostMapping("/{id}/answer")
    public void answer(@PathVariable Long id,
                       @RequestBody AnswerDto answer,
                       Principal principal) {
        service.answerQuestion(id, answer.getQuestionId(), answer.getSelectedAnswerIndex(), principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDto> getSession(@PathVariable Long id, Principal principal) {
        GameSession session = service.getSession(id, principal.getName());
        return ResponseEntity.ok(service.toDto(session));
    }

    @GetMapping("/completed")
    public List<GameSessionDto> getCompletedSession(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userName = jwtService.extractUsername(token);
        List<GameSession> gameSessions = service.getCompletedSessions(userName);
        List<GameSessionDto> gameSessionDtos = new ArrayList<>();
        for (GameSession gameSession : gameSessions) {
            GameSessionDto dto = service.toDto(gameSession);
            gameSessionDtos.add(dto);
        }
        return gameSessionDtos;
    }


    @GetMapping("/open")
    public List<GameSessionDto> getOpen(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userName = jwtService.extractUsername(token);
        List<GameSession> gameSessions = service.getOpenSessions(userName);
        List<GameSessionDto> gameSessionDtos = new ArrayList<>();
        for (GameSession gameSession : gameSessions) {
            GameSessionDto dto = service.toDto(gameSession);
            gameSessionDtos.add(dto);
        }
        return gameSessionDtos;
    }

    @PostMapping("/{id}/update-indexAndScore")
    public ResponseEntity<Void> updateIndexAndScore(@PathVariable Long id,
                                            @RequestBody Map<String, Integer> body,
                                            Principal principal) {
        Integer index = body.get("index");
        int score = body.get("score");
        service.updateCurrentIndexAndScore(id, index, score, principal.getName());
        return ResponseEntity.ok().build();
    }



    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeSession(@PathVariable Long id, Principal principal) {
        service.markSessionAsCompleted(id, principal.getName());
        return ResponseEntity.ok().build();
    }

}

