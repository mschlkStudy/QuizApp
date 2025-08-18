package com.quiz.quizapp.service;

import com.quiz.quizapp.dto.CoopSessionDto;
import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.dto.UserDto;
import com.quiz.quizapp.entity.CoopSession;

import com.quiz.quizapp.entity.Question;
import com.quiz.quizapp.entity.User;
import com.quiz.quizapp.repository.CoopSessionRepository;
import com.quiz.quizapp.repository.QuestionRepository;
import com.quiz.quizapp.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoopSessionService {

    private final CoopSessionRepository coopRepo;
    private final UserRepository userRepo;
    private final QuestionRepository questionRepo;
    private final QuestionService questionService;

    public CoopSessionService(CoopSessionRepository coopRepo, UserRepository userRepo, QuestionRepository questionRepo, QuestionService questionService) {
        this.coopRepo = coopRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.questionService = questionService;
    }

    public CoopSession createSession(String username, Long subjectId, Long modulId, int amount, String subjectName, String modulName) {
        User user = userRepo.findByUsername(username).orElseThrow();
        CoopSession session = new CoopSession();
        session.addPlayer(user);
        List<Question> questions = questionRepo.findRandomBySubjectModulId(modulId,amount);
        session.setQuestions(questions);
        session.setSubjectId(subjectId);
        session.setModulId(modulId);
        session.setSubjectName(subjectName);
        session.setModulName(modulName);
        coopRepo.save(session);
        return coopRepo.findById(session.getId()).orElseThrow();
    }


    public List<CoopSession> getOpenSessions() {
        return coopRepo.findByStatus(CoopSession.CoopStatus.WAITING_FOR_PLAYERS);
    }

    public List<CoopSession> getCompletedSessions() {
        return coopRepo.findByStatus(CoopSession.CoopStatus.COMPLETED);
    }

    public CoopSession joinSession(Long sessionId, String username) {
        CoopSession session = coopRepo.findById(sessionId).orElseThrow();
        User user = userRepo.findByUsername(username).orElseThrow();

        if (!session.getPlayers().contains(user)) {
            session.addPlayer(user);
        }

        if (session.getPlayers().size() == 4) {
            session.setStatus(CoopSession.CoopStatus.IN_PROGRESS);
            session.setStartedAt(LocalDateTime.now());
        }
        return coopRepo.save(session);
    }

    public CoopSessionDto startOrJoinSession(Long subjectId, Long modulId, int amount, String username, String subjectName, String modulName) {
        // - Suche offene Session, max. 4 Spieler, korrektes Subject/Modul, User noch nicht dabei
        CoopSession openSession = coopRepo.findBySubjectIdAndModulIdAndStatus(subjectId, modulId, CoopSession.CoopStatus.WAITING_FOR_PLAYERS)
                .stream()
                .filter(session -> session.getPlayers().size() < 4 &&
                        session.getPlayers().stream().noneMatch(u -> u.getUsername().equals(username)))
                .findFirst()
                .orElse(null);

        User user = userRepo.findByUsername(username).orElseThrow();

        if (openSession != null) {
            openSession.addPlayer(user);

            if (openSession.getPlayers().size() == 4) {
                openSession.setStatus(CoopSession.CoopStatus.IN_PROGRESS);
                openSession.setStartedAt(LocalDateTime.now());
            }

            coopRepo.save(openSession);
            return toDto(openSession);
        }

        // -> Lege eine neue Session an
        CoopSession session = new CoopSession();
        session.addPlayer(user);
        List<Question> questions = questionRepo.findRandomBySubjectModulId(modulId, amount);
        session.setQuestions(questions);
        session.setSubjectId(subjectId);
        session.setModulId(modulId);
        session.setSubjectName(subjectName);
        session.setModulName(modulName);
        session.setStatus(CoopSession.CoopStatus.WAITING_FOR_PLAYERS);
        coopRepo.save(session);

        return toDto(session);
    }



    public CoopSessionDto toDto(CoopSession coopSession) {
        List<UserDto> players = coopSession.getPlayers().stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : coopSession.getQuestions()) {
           questionDtos.add(QuestionDto.fromEntity(question));
        }

        return new CoopSessionDto(
                coopSession.getId(),
                coopSession.getStatus().name(),
                players,
                coopSession.getStartedAt(),
                questionDtos,
                coopSession.getSubjectName(),
                coopSession.getModulName(),
                coopSession.getSubjectId(),
                coopSession.getModulId()
        );
    }

    private UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}


