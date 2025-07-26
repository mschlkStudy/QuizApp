package com.quiz.quizapp.mapper;

import com.quiz.quizapp.dto.SubjectModulDto;
import com.quiz.quizapp.entity.SubjectModul;

public class SubjectModulMapper {

    public static SubjectModulDto toDto(SubjectModul modul) {
        SubjectModulDto dto = new SubjectModulDto(modul.getId(), modul.getName());
        dto.setId(modul.getId());
        dto.setName(modul.getName());
        return dto;
    }
}

