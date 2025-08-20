package com.quiz.quizapp.dto;

public class AnswerDto
{

    private Long questionId;
    private Integer selectedAnswerIndex;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }

    public void setSelectedAnswerIndex(Integer selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }
}
