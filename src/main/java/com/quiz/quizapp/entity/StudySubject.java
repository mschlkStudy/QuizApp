package com.quiz.quizapp.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class StudySubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "subject_modul",
            joinColumns = @JoinColumn(name = "study_subject_id"),
            inverseJoinColumns = @JoinColumn(name ="module_id")
    )
    private Set<SubjectModul> modules = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<SubjectModul> getModules() {
        return modules;
    }

    public void setModules(Set<SubjectModul> modules) {
        this.modules = modules;
    }
}
