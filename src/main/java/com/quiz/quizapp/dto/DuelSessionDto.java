package com.quiz.quizapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class DuelSessionDto {
    private Long id;
    private String player1;
    private String player2;
    private String status; // WAITING_FOR_PLAYER, IN_PROGRESS, COMPLETED
    private List<QuestionDto> questions;
    private Integer currentQuestionIndex;
    private Integer score;
    private LocalDateTime startedAt;
    private String subjectName;
    private String modulName;

    public DuelSessionDto() {}

    public DuelSessionDto(Long id, String player1, String player2, String status, List<QuestionDto> questions, Integer currentQuestionIndex, Integer score, LocalDateTime startedAt, String subjectName, String modulName) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.status = status;
        this.questions = questions;
        this.currentQuestionIndex = currentQuestionIndex;
        this.score = score;
        this.startedAt = startedAt;
        this.subjectName = subjectName;
        this.modulName = modulName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public Integer getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
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
}

