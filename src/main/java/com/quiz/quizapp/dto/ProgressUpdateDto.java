package com.quiz.quizapp.dto;

import lombok.Data;


public class ProgressUpdateDto {
    private int index;
    private int score;

    public ProgressUpdateDto(int index, int score) {
        this.index = index;
        this.score = score;
    }

    public ProgressUpdateDto() {

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

