package com.quiz.quizapp.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.quizapp.dto.QuestionDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "duel_sessions")
public class DuelSession {
    public enum DuelStatus {
        WAITING_FOR_PLAYER,
        IN_PROGRESS,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1;
    private String player2;

    private Long subjectId;
    private String subjectName;

    private Long modulId;
    private String modulName;

    @Enumerated(EnumType.STRING)
    private DuelStatus status;

    @ManyToMany
    @JoinTable(
            name = "duel_session_questions",
            joinColumns = @JoinColumn(name = "duel_session_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    List<Question> questions;

    // Fortschritt & Punkte
    private Integer player1Index;
    private Integer player2Index;

    private Integer player1Score;
    private Integer player2Score;

    private Boolean player1Finished = false;
    private Boolean player2Finished = false;

    private String winner;


    private LocalDateTime startedAt;

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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getModulId() {
        return modulId;
    }

    public void setModulId(Long modulId) {
        this.modulId = modulId;
    }

    public String getModulName() {
        return modulName;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }

    public DuelStatus getStatus() {
        return status;
    }

    public void setStatus(DuelStatus status) {
        this.status = status;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Integer getPlayer1Index() {
        return player1Index;
    }

    public void setPlayer1Index(Integer player1Index) {
        this.player1Index = player1Index;
    }

    public Integer getPlayer2Index() {
        return player2Index;
    }

    public void setPlayer2Index(Integer player2Index) {
        this.player2Index = player2Index;
    }

    public Integer getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(Integer player1Score) {
        this.player1Score = player1Score;
    }

    public Integer getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(Integer player2Score) {
        this.player2Score = player2Score;
    }

    public Boolean getPlayer1Finished() {
        return player1Finished;
    }

    public void setPlayer1Finished(Boolean player1Finished) {
        this.player1Finished = player1Finished;
    }

    public Boolean getPlayer2Finished() {
        return player2Finished;
    }

    public void setPlayer2Finished(Boolean player2Finished) {
        this.player2Finished = player2Finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    public void setStartedAt(LocalDateTime startedAt) {
    }
}
