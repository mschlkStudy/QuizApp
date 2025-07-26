package com.quiz.quizapp.dto;

import java.util.List;

public class QuestionDto {
    private Long id;
    private String questionText;
    private List<String> answers;
    private Integer correctAnswerIndex;
    private Long subjectModulId;
    private String subjectModulName;

    public Long getStudySubjectId() {
        return studySubjectId;
    }

    public void setStudySubjectId(Long studySubjectId) {
        this.studySubjectId = studySubjectId;
    }

    private Long studySubjectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Integer getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(Integer correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public Long getSubjectModulId() {
        return subjectModulId;
    }

    public void setSubjectModulId(Long subjectModulId) {
        this.subjectModulId = subjectModulId;
    }

    public String getSubjectModulName() {
        return subjectModulName;
    }

    public void setSubjectModulName(String subjectModulName) {
        this.subjectModulName = subjectModulName;
    }

}
