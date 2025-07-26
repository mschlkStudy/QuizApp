package com.quiz.quizapp.mapper;

import com.quiz.quizapp.dto.StudySubjectDto;
import com.quiz.quizapp.entity.StudySubject;

public class StudySubjectMapper {

    public static StudySubjectDto toDto(StudySubject subject) {
        StudySubjectDto dto = new StudySubjectDto(subject.getId(), subject.getName());
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        return dto;
    }
}

