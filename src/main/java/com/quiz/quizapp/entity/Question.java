package com.quiz.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ElementCollection
    private List<String> answers;

    private Integer correctAnswerIndex;

    @ManyToOne
    @JoinColumn(name="study_subject_id")
    private StudySubject studySubject;

    public StudySubject getStudySubject() {
        return studySubject;
    }

    public void setStudySubject(StudySubject studySubject) {
        this.studySubject = studySubject;
    }

    @ManyToOne
    @JoinColumn(name = "modul_id")
    @JsonIgnore
    private SubjectModul subjectModul;

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

    public SubjectModul getSubjectModul() {
        return subjectModul;
    }

    public void setSubjectModul(SubjectModul subjectModul) {
        this.subjectModul = subjectModul;
    }


}