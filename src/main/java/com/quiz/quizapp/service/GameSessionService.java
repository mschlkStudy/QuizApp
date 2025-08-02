package com.quiz.quizapp.service;

import com.quiz.quizapp.entity.*;
import com.quiz.quizapp.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameSessionService {

    private final GameSessionRepository sessionRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;

    public GameSessionService(GameSessionRepository sessionRepo, QuestionRepository questionRepo, UserRepository userRepo) {
        this.sessionRepo = sessionRepo;
        this.questionRepo = questionRepo;
        this.userRepo = userRepo;
    }

    public GameSession createNewSession(String username, Long subjectId, Long modulId, int amount) {
        User user = userRepo.findByUsername(username).orElseThrow();
        List<Question> questions = questionRepo.findRandomBySubjectModulId(modulId, amount);

        GameSession session = new GameSession();
        session.setUser(user);
        session.setStudySubject(new StudySubject(subjectId));
        session.setSubjectModul(new SubjectModul(modulId));

        List<GameSessionQuestion> sessionQuestions = questions.stream().map(q -> {
            GameSessionQuestion gq = new GameSessionQuestion();
            gq.setQuestion(q);
            gq.setGivenAnswerIndex(null);
            gq.setGameSession(session);
            return gq;
        }).toList();

        session.setQuestions(sessionQuestions);
        return sessionRepo.save(session);
    }

    public void answerQuestion(Long sessionId, Long questionId, int answerIndex, String username) {
        GameSession session = sessionRepo.findByIdAndUserUsername(sessionId, username).orElseThrow();

        session.getQuestions().stream()
                .filter(q -> q.getQuestion().getId().equals(questionId))
                .findFirst()
                .ifPresent(q -> q.setGivenAnswerIndex(answerIndex));

        sessionRepo.save(session);
    }

    public GameSession getSession(Long id, String username) {
        return sessionRepo.findByIdAndUserUsername(id, username).orElseThrow();
    }

    public List<GameSession> getOpenSessions(String username) {
        return sessionRepo.findByUserUsernameAndCompletedFalse(username);
    }

    public void markAsCompleted(Long id, String username) {
        GameSession session = sessionRepo.findByIdAndUserUsername(id, username).orElseThrow();
        session.setCompleted(true);
        sessionRepo.save(session);
    }
}
