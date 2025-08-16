package com.quiz.quizapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CompletedDuelDto {

    private Long duelId;

    private String player1;
    private int player1Score;

    private String player2;
    private int player2Score;

    private String winner; // username oder "Unentschieden"

    private Long subjectId;
    private String subjectName;

    private Long modulId;
    private String modulName;

    private String status; // "COMPLETED"




    public CompletedDuelDto() {}

    public CompletedDuelDto(Long duelId, String player1, int player1Score, String player2, int player2Score, String subjectName, String modulName, String status, String winner, Long subjectId, Long modulId) {
        this.winner = winner;
        this.status = status;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.duelId = duelId;
        this.subjectId = subjectId;
        this.modulId = modulId;
        this.player1 = player1;
        this.player2 = player2;
        this.subjectName = subjectName;
        this.modulName = modulName;
    }

    public Long getDuelId() {
        return duelId;
    }

    public void setDuelId(Long duelId) {
        this.duelId = duelId;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

