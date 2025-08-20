package com.quiz.quizapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CoopSessionDto {
    private Long id;
    private final String status;
    private List<UserDto> players;
    private LocalDateTime startedAt;
    private List<QuestionDto> questions;
    private String subjectName;
    private String modulName;
    private Long subjectId;
    private Long modulId;
    private int currentQuestionIndex;
    private int score;

    public CoopSessionDto(Long id, String status, List<UserDto> players, LocalDateTime startedAt, List<QuestionDto> questions, String subjectName, String modulName, Long subjectId, Long modulId, int currentQuestionIndex, int score) {
        this.id = id;
        this.status = status;
        this.players = players;
        this.startedAt = startedAt;
        this.questions = questions;
        this.subjectName = subjectName;
        this.modulName = modulName;
        this.subjectId = subjectId;
        this.modulId = modulId;
        this.currentQuestionIndex = currentQuestionIndex;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<UserDto> getPlayers() {
        return players;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuestionDto> questions) {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getModulName() {
        return modulName;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getModulId() {
        return modulId;
    }

    public void setModulId(Long modulId) {
        this.modulId = modulId;
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
}

