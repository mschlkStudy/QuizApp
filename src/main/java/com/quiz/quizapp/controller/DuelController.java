package com.quiz.quizapp.controller;

import com.quiz.quizapp.dto.DuelSessionDto;
import com.quiz.quizapp.dto.ProgressUpdateDto;
import com.quiz.quizapp.dto.CompletedDuelDto;
import com.quiz.quizapp.entity.DuelSession;
import com.quiz.quizapp.security.JwtService;
import com.quiz.quizapp.service.DuelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/duels")
public class DuelController {

    private final DuelService duelService;
    private final JwtService jwtService;

    public DuelController(DuelService duelService, JwtService jwtService) {
        this.duelService = duelService;
        this.jwtService = jwtService;
    }



    /**
     * Startet ein neues Duell oder tritt einem offenen Duell bei.
     */
    @PostMapping("/start-or-join")
    public ResponseEntity<DuelSessionDto> startOrJoinDuel(
            @RequestParam Long subjectId,
            @RequestParam Long modulId,
            @RequestParam String modulName,
            @RequestParam String subjectName,
            @RequestParam(defaultValue = "10") int amount,
            Principal principal
    ) {
        String username = principal.getName();
        DuelSessionDto duelSession = duelService.startOrJoin(subjectId, modulId, amount, username, modulName, subjectName);
        return ResponseEntity.ok(duelSession);
    }

    /**
     * Aktualisiert den Fortschritt eines Spielers im Duell.
     */
    @PostMapping("/{duelId}/update-progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long duelId,
            @RequestBody ProgressUpdateDto progress,
            Principal principal
    ) {
        duelService.updateProgress(duelId, principal.getName(), progress.getIndex(), progress.getScore());
        return ResponseEntity.ok().build();
    }

    /**
     * Markiert das Duell für einen Spieler als abgeschlossen.
     */
    @PostMapping("/{duelId}/complete")
    public ResponseEntity<Void> completeDuel(
            @PathVariable Long duelId,
            Principal principal
    ) {
        duelService.completeDuel(duelId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/overview/open")
    public List<DuelSessionDto> getOpenDuelSessions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userName = jwtService.extractUsername(token);
        List<DuelSession> duelSessions = duelService.getOpenDuels(userName);
        List<DuelSessionDto> duelSessionDtos = new ArrayList<>();
        for (DuelSession duelSession : duelSessions) {
            DuelSessionDto dto = duelService.mapToDto(duelSession, userName);
            duelSessionDtos.add(dto);
        }
        return duelSessionDtos;
    }

    @GetMapping("/overview/completed")
    public List<CompletedDuelDto> getCompletedDuelSessions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userName = jwtService.extractUsername(token);
        List<DuelSession> duelSessions =  duelService.getCompletedDuels(userName);
        List<CompletedDuelDto> completedDuelDtos = new ArrayList<>();
        for (DuelSession duelSession : duelSessions) {
            CompletedDuelDto dto = duelService.mapToCompletedDto(duelSession, userName);
            completedDuelDtos.add(dto);
        }
        return completedDuelDtos;
    }
    @GetMapping("/{duelId}")
    public ResponseEntity<DuelSessionDto> getDuelById(@PathVariable Long duelId, Principal principal) {
        DuelSessionDto duel = duelService.getDuelById(duelId, principal.getName());
        return ResponseEntity.ok(duel);
    }
}
