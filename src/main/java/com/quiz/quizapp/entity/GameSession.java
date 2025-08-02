package com.quiz.quizapp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean completed = false;

    @ManyToOne
    private User user;

    @ManyToOne
    private StudySubject studySubject;

    @ManyToOne
    private SubjectModul subjectModul;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSessionQuestion> questions;

    public GameSession() {}

    public GameSession(Long id, boolean completed, User user, StudySubject studySubject, SubjectModul subjectModul, List<GameSessionQuestion> questions) {
        this.id = id;
        this.completed = completed;
        this.user = user;
        this.studySubject = studySubject;
        this.subjectModul = subjectModul;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudySubject getStudySubject() {
        return studySubject;
    }

    public void setStudySubject(StudySubject studySubject) {
        this.studySubject = studySubject;
    }

    public SubjectModul getSubjectModul() {
        return subjectModul;
    }

    public void setSubjectModul(SubjectModul subjectModul) {
        this.subjectModul = subjectModul;
    }

    public List<GameSessionQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<GameSessionQuestion> questions) {
        this.questions = questions;
    }

    // Getter, Setter, Konstruktoren
}

