package com.quiz.quizapp.controller;

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

    @PostMapping("/start-or-join")
    public CoopSessionDto startOrJoinSession(@RequestHeader("Authorization") String authHeader,
                                             @RequestParam Long subjectId,
                                             @RequestParam Long modulId,
                                             @RequestParam int amount,
                                             @RequestParam String subjectName,
                                             @RequestParam String modulName) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CoopSessionDto dto = coopService.startOrJoinSession(subjectId, modulId, amount, username, subjectName, modulName);
        return dto;
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


