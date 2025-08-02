package com.quiz.quizapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalQuestions;
    private int correctAnswers;

    private LocalDateTime playedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public GameResult() {
    }

    public GameResult(Long id, int totalQuestions, int correctAnswers, LocalDateTime playedAt, User user) {
        this.id = id;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.playedAt = playedAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

