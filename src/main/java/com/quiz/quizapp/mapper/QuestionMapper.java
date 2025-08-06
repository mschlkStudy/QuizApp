package com.quiz.quizapp.mapper;

import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.entity.Question;
import com.quiz.quizapp.entity.SubjectModul;

import java.util.List;

public class QuestionMapper {

    public static QuestionDto toDto(Question question) {
        QuestionDto dto = QuestionDto.fromEntity(question);
        dto.setId(question.getSubjectModul().getId());
        return dto;
    }

    public static Question fromDto(QuestionDto dto, SubjectModul modul) {
        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setAnswers(dto.getAnswers());
        question.setCorrectAnswerIndex(dto.getCorrectAnswerIndex());
        question.setSubjectModul(modul);
        return question;
    }
}
