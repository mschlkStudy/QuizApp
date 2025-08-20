package com.quiz.quizapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coop_sessions")
public class CoopSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CoopStatus status = CoopStatus.WAITING_FOR_PLAYERS;

    @ManyToMany
    @JoinTable(
            name = "coop_session_players",
            joinColumns = @JoinColumn(name = "coop_session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "coop_session_questions",
            joinColumns = @JoinColumn(name = "coop_session_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    private LocalDateTime startedAt;
    private Long subjectId;
    private Long modulId;
    private String subjectName;
    private String modulName;
    @Column(name = "current_question_index", nullable = true)
    private int currentQuestionIndex;
    @Column(nullable = true)
    private int score;

    public enum CoopStatus {
        WAITING_FOR_PLAYERS,
        IN_PROGRESS,
        COMPLETED
    }

    public boolean canJoin() {
        return players.size() < 4 && status == CoopStatus.WAITING_FOR_PLAYERS;
    }

    public void addPlayer(User user) {
        if (canJoin() && !players.contains(user)) {
            players.add(user);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoopStatus getStatus() {
        return status;
    }

    public void setStatus(CoopStatus status) {
        this.status = status;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public Long getSubjectId() {

        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getModulId() {
        return modulId;
    }

    public void setModulId(Long modulId) {
        this.modulId = modulId;
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

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}


