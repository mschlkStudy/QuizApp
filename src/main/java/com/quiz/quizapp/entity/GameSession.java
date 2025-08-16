package com.quiz.quizapp.entity;

import jakarta.persistence.*;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GameSession {

    public enum SessionMode {
        SINGLE, DUEL, WAITING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean completed = false;

    @ManyToOne
    private User user;

    @ManyToOne
    private StudySubject studySubject;

    @ManyToOne
    private SubjectModul subjectModul;

    @ManyToMany
    @JoinTable(
            name = "game_session_questions",
            joinColumns = @JoinColumn(name = "game_session_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;

    @Column(nullable = true)
    private int currentQuestionIndex = 0;
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private int score;

    private SessionMode mode = SessionMode.SINGLE;

    public GameSession() {}

    public GameSession(Long id, boolean completed, User user, StudySubject studySubject, SubjectModul subjectModul, List<Question> questions, int score,  SessionMode mode) {
        this.id = id;
        this.completed = completed;
        this.user = user;
        this.studySubject = studySubject;
        this.subjectModul = subjectModul;
        this.questions = questions;
        this.score = score;
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudySubject getStudySubject() {
        return studySubject;
    }

    public void setStudySubject(StudySubject studySubject) {
        this.studySubject = studySubject;
    }

    public SubjectModul getSubjectModul() {
        return subjectModul;
    }

    public void setSubjectModul(SubjectModul subjectModul) {
        this.subjectModul = subjectModul;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SessionMode getMode() {
        return mode;
    }

    public void setMode(SessionMode mode) {
        this.mode = mode;
    }
}

