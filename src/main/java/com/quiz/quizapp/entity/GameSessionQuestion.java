package com.quiz.quizapp.entity;

import jakarta.persistence.*;

@Entity
public class GameSessionQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GameSession gameSession;

    @ManyToOne
    private Question question;

    private Integer givenAnswerIndex;

    public GameSessionQuestion() {
    }

    public GameSessionQuestion(Long id, GameSession gameSession, Question question, Integer givenAnswerIndex) {
        this.id = id;
        this.gameSession = gameSession;
        this.question = question;
        this.givenAnswerIndex = givenAnswerIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getGivenAnswerIndex() {
        return givenAnswerIndex;
    }

    public void setGivenAnswerIndex(Integer givenAnswerIndex) {
        this.givenAnswerIndex = givenAnswerIndex;
    }
}

