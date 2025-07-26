package com.quiz.quizapp.controller;

import com.quiz.quizapp.dto.StudySubjectDto;
import com.quiz.quizapp.dto.SubjectModulDto;
import com.quiz.quizapp.entity.SubjectModul;
import com.quiz.quizapp.repository.StudySubjectRepository;
import com.quiz.quizapp.repository.SubjectModulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dropdowns")
public class DropDownController {

    @Autowired
    private StudySubjectRepository studySubjectRepository;

    @Autowired
    private SubjectModulRepository subjectModulRepository;

    @GetMapping("/study-subjects")
    public List<StudySubjectDto> getStudySubjects() {
        return studySubjectRepository.findAll()
                .stream()
                .map(subject -> {
                    System.out.println("Subject: id=" + subject.getId() + ", name=" + subject.getName());
                    return new StudySubjectDto(subject.getId(), subject.getName());
                })
                .toList();
    }


    @GetMapping("/subject-moduls")
    public List<SubjectModulDto> getSubjectModuls() {
        return subjectModulRepository.findAll()
                .stream()
                .map(modul -> new SubjectModulDto(modul.getId(), modul.getName()))
                .toList();
    }

    @GetMapping("/subject-modules/{studySubjectId}")
    public List<SubjectModulDto> getModulesByStudySubject(@PathVariable Long studySubjectId) {
        List<SubjectModul> modules = studySubjectRepository.findModulesByStudySubjectId(studySubjectId);
        return modules.stream()
                .map(modul -> new SubjectModulDto(modul.getId(), modul.getName()))
                .toList();
    }

}
