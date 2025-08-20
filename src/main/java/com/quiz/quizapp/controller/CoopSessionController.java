package com.quiz.quizapp.controller;

import com.quiz.quizapp.dto.AnswerDto;
import com.quiz.quizapp.dto.CoopSessionDto;
import com.quiz.quizapp.entity.CoopSession;
import com.quiz.quizapp.security.JwtService;
import com.quiz.quizapp.service.CoopSessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coop-session")
public class CoopSessionController {

    private final CoopSessionService coopService;
    private final JwtService jwtService;

    public CoopSessionController(CoopSessionService coopService,  JwtService jwtService) {
        this.coopService = coopService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public CoopSessionDto createSession(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam Long subjectId,
                                        @RequestParam Long modulId,
                                        @RequestParam int amount,
                                        @RequestParam String subjectName,
                                        @RequestParam String modulName) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSession session = coopService.createSession(username, subjectId, modulId, amount, subjectName, modulName);
        CoopSessionDto dto = coopService.toDto(session);
        return dto;
    }
    @PostMapping("/{id}/answer")
    public CoopSessionDto answerQuestion(
            @PathVariable Long id,
            @RequestBody AnswerDto answerDto,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSession updatedSession = coopService.answerQuestion(id, answerDto, username);
        return coopService.toDto(updatedSession);
    }


    @PostMapping("/start-or-join")
    public CoopSessionDto startSession(@RequestHeader("Authorization") String authHeader,
                                             @RequestParam Long subjectId,
                                             @RequestParam Long modulId,
                                             @RequestParam int amount,
                                             @RequestParam String subjectName,
                                             @RequestParam String modulName) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSessionDto dto = coopService.startSession(subjectId, modulId, amount, username, subjectName, modulName);
        return dto;
    }

    @PostMapping("/{id}/save-progress")
    public CoopSessionDto saveProgress(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer currentQuestionIndex,
            @RequestParam(required = false) Integer score) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSession session = coopService.saveProgress(id, username, currentQuestionIndex, score);
        return coopService.toDto(session);
    }

    @GetMapping("/overview/open")
    public List<CoopSessionDto> getOpenSessions() {
        return coopService.getOpenSessions().stream()
                .map(coopService::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/overview/completed")
    public List<CoopSessionDto> getCompletedSessions() {
        return coopService.getCompletedSessions().stream()
                .map(coopService::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/join")
    public CoopSessionDto joinSession(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSession session = coopService.joinSession(id, username);
        return coopService.toDto(session);
    }

}


