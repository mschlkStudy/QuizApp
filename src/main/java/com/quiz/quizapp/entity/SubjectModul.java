package com.quiz.quizapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subject_modul")
public class SubjectModul {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "modules")
    private Set<StudySubject> studySubjects = new HashSet<>();

    @OneToMany(mappedBy = "subjectModul", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();


    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<StudySubject> getStudySubjects() { return studySubjects; }
    public void setStudySubjects(Set<StudySubject> studySubjects) { this.studySubjects = studySubjects; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

}

