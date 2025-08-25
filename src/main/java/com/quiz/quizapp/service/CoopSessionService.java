package com.quiz.quizapp.service;

import com.quiz.quizapp.dto.AnswerDto;
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

    public CoopSessionService(CoopSessionRepository coopRepo, UserRepository userRepo, QuestionRepository questionRepo) {
        this.coopRepo = coopRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
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

    public CoopSession saveProgress(Long sessionId, String username, Integer currentQuestionIndex, Integer score) {
        CoopSession session = coopRepo.findById(sessionId).orElseThrow();

        boolean isPlayer = session.getPlayers().stream().anyMatch(u -> u.getUsername().equals(username));
        if (!isPlayer) {
            throw new IllegalArgumentException("User is not part of this session");
        }

        if (currentQuestionIndex != null) {
            session.setCurrentQuestionIndex(currentQuestionIndex);
        }
        if (score != null) {
            session.setScore(score);
        }

        coopRepo.save(session);
        return session;
    }

    public CoopSession answerQuestion(Long sessionId, AnswerDto answerDto, String username) {
        CoopSession session = coopRepo.findById(sessionId).orElseThrow();

        boolean isPlayer = session.getPlayers().stream()
                .anyMatch(u -> u.getUsername().equals(username));
        if (!isPlayer) {
            throw new IllegalArgumentException("User is not part of this coop session");
        }

        if (session.getStatus() == CoopSession.CoopStatus.COMPLETED) {
            throw new IllegalStateException("Session already completed!");
        }

        int idx = session.getCurrentQuestionIndex();
        List<Question> questions = session.getQuestions();

        // Wenn keine weiteren Fragen mehr offen sind oder Index falsch: sauber abbrechen
        if (questions == null || idx >= questions.size()) {
            // Status jetzt sicher setzen
            session.setStatus(CoopSession.CoopStatus.COMPLETED);
            coopRepo.save(session);
            throw new IllegalStateException("Keine weiteren Fragen vorhanden. Die Session ist bereits abgeschlossen.");
        }

        Question currentQuestion = questions.get(idx);

//        if (!currentQuestion.getId().equals(answerDto.getQuestionId())) {
//            throw new IllegalArgumentException("Falsche Frage");
//        }

        if (currentQuestion.getCorrectAnswerIndex() == answerDto.getSelectedAnswerIndex()) {
            session.setScore(session.getScore() + 1);
        }
        session.setCurrentQuestionIndex(idx + 1);

        if (session.getCurrentQuestionIndex() >= questions.size()) {
            session.setStatus(CoopSession.CoopStatus.COMPLETED);
        }

        return coopRepo.save(session);
    }



    public CoopSessionDto startSession(Long subjectId, Long modulId, int amount, String username, String subjectName, String modulName) {

        User user = userRepo.findByUsername(username).orElseThrow();
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
                coopSession.getModulId(),
                coopSession.getCurrentQuestionIndex(),
                coopSession.getScore()
        );
    }

    private UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}


