package com.quiz.quizapp.dto;

import com.quiz.quizapp.entity.GameSession;
import com.quiz.quizapp.entity.StudySubject;
import com.quiz.quizapp.entity.SubjectModul;
import com.quiz.quizapp.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class GameSessionDto {
    private Long id;
    private User user;
    private StudySubject studySubject;
    private SubjectModul modul;
    private List<QuestionDto> questions;
    private String studySubjectName;
    private String modulName;
    private String username;
    private int currentQuestionIndex;
    private boolean completed = false;
    private LocalDateTime startedAt;
    private int score;
    private GameSession.SessionMode mode;



    public GameSessionDto(Long id, String studySubjectName, String modulName, List<QuestionDto> questionDtos, String username, int currentQuestionIndex, boolean completed, LocalDateTime startedAt, int score, GameSession.SessionMode mode) {
        this.id = id;
        this.studySubjectName = studySubjectName;
        this.modulName = modulName;
        this.questions = questionDtos;
        this.username = username;
        this.currentQuestionIndex = currentQuestionIndex;
        this.completed = completed;
        this.startedAt = startedAt;
        this.score = score;
        this.mode = mode;
    }

    public GameSessionDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SubjectModul getModul() {
        return modul;
    }

    public void setModul(SubjectModul modul) {
        this.modul = modul;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public String getStudySubjectName() {
        return studySubjectName;
    }

    public void setStudySubjectName(String studySubjectName) {
        this.studySubjectName = studySubjectName;
    }

    public String getModulName() {
        return modulName;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameSession.SessionMode getMode() {
        return mode;
    }

    public void setMode(GameSession.SessionMode mode) {
        this.mode = mode;
    }
}
