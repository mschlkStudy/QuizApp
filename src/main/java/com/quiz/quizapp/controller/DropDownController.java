package com.quiz.quizapp.controller;

import com.quiz.quizapp.entity.StudySubject;
import com.quiz.quizapp.entity.SubjectModul;
import com.quiz.quizapp.repository.StudySubjectRepository;
import com.quiz.quizapp.repository.SubjectModulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dropdowns")
@PreAuthorize("hasRole('User')")
public class DropDownController {

    @Autowired
    private StudySubjectRepository studySubjectRepository;

    @Autowired
    private SubjectModulRepository subjectModulRepository;

    @GetMapping("/study-subjects")
    public List<StudySubject> getStudySubjects() {
        return studySubjectRepository.findAll();
    }

    @GetMapping("/subject-moduls")
    public List<SubjectModul> getSubjectModuls() {
        return subjectModulRepository.findAll();
    }
}
