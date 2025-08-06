package com.quiz.quizapp.service;

import com.quiz.quizapp.dto.GameSessionDto;
import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.entity.*;
import com.quiz.quizapp.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameSessionService {

    private final GameSessionRepository sessionRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;
    private final StudySubjectRepository subjectRepo;
    private final SubjectModulRepository modulRepo;

    public GameSessionService(GameSessionRepository sessionRepo, QuestionRepository questionRepo, UserRepository userRepo, StudySubjectRepository subjectRepo, SubjectModulRepository modulRepo) {
        this.sessionRepo = sessionRepo;
        this.questionRepo = questionRepo;
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
        this.modulRepo = modulRepo;
    }

    public GameSession createNewSession(String username, Long subjectId, Long modulId, int amount) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudySubject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("StudySubject not found"));

        SubjectModul modul = modulRepo.findById(modulId)
                .orElseThrow(() -> new RuntimeException("Modul not found"));

        List<Question> questions = questionRepo.findRandomBySubjectModulId(modulId, amount);
        if (questions.isEmpty()) {
            throw new RuntimeException("Keine Fragen gefunden");
        }

        GameSession session = new GameSession();
        session.setUser(user);
        session.setStudySubject(subject);
        session.setSubjectModul(modul);
        session.setQuestions(questions);
        session.setCurrentQuestionIndex(0);
        session.setStartedAt(LocalDateTime.now());
        session.setCompleted(false);

        return sessionRepo.save(session);
    }



    public void answerQuestion(Long sessionId, Long questionId, int answerIndex, String username) {
        GameSession session = sessionRepo.findByIdAndUserUsername(sessionId, username).orElseThrow();

        session.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .ifPresent(q -> q.setCorrectAnswerIndex(answerIndex));

        sessionRepo.save(session);
    }

    public GameSession getSession(Long id, String username) {
        return sessionRepo.findByIdAndUserUsername(id, username).orElseThrow();
    }

    public List<GameSession> getOpenSessions(String username) {
        return sessionRepo.findByUserUsernameAndCompletedFalse(username);
    }

    public void markSessionAsCompleted(Long id, String username) {
        GameSession session = sessionRepo.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        session.setCompleted(true);
        sessionRepo.save(session);
    }

    public void updateCurrentIndexAndScore(Long id, int index, int score, String username) {
        GameSession session = sessionRepo.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        session.setCurrentQuestionIndex(index);
        session.setScore(score);
        sessionRepo.save(session);
    }

    public List<GameSession> getCompletedSessions(String username) {
        return sessionRepo.findByUserUsernameAndCompletedTrue(username);
    }



    @Transactional(readOnly = true)
    public GameSessionDto toDto(GameSession session) {
        List<QuestionDto> questionDtos = new ArrayList<>();
        Long id = session.getId();
        String username = session.getUser().getUsername();
        String subjectName = session.getStudySubject().getName();
        String modulName = session.getSubjectModul().getName();
        String mode = session.getMode();
        LocalDateTime startedAt = session.getStartedAt();
        int score = session.getScore();
        boolean completed = session.isCompleted();
        int currentQuestionIndex = session.getCurrentQuestionIndex();
        for (Question question : session.getQuestions()) {
            QuestionDto questionDto = QuestionDto.fromEntity(question);
            questionDtos.add(questionDto);
        }

        return new GameSessionDto(id, subjectName, modulName, questionDtos, username, currentQuestionIndex, completed, startedAt, score, mode);
    }


}
