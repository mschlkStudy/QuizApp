package com.quiz.quizapp.security;

import com.quiz.quizapp.entity.StudySubject;
import com.quiz.quizapp.entity.SubjectModul;
import com.quiz.quizapp.repository.StudySubjectRepository;
import com.quiz.quizapp.repository.SubjectModulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownService {

    @Autowired
    StudySubjectRepository studySubjectRepository;

    @Autowired
    SubjectModulRepository  subjectModulRepository;

    public List<StudySubject> getAllStudySubjects() {
        return studySubjectRepository.findAll();
    }

    public List<SubjectModul> getAllSubjectModuls() {
        return subjectModulRepository.findAll();
    }
}
