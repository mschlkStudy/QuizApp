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

    public CoopSessionDto(Long id, String status, List<UserDto> players, LocalDateTime startedAt, List<QuestionDto> questions, String subjectName, String modeName) {
        this.id = id;
        this.status = status;
        this.players = players;
        this.startedAt = startedAt;
        this.questions = questions;
        this.subjectName = subjectName;
        this.modulName = modeName;
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
}

