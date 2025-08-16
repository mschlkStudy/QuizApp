package com.quiz.quizapp.service;
import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.entity.Question;
import com.quiz.quizapp.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Holt eine Liste zufälliger Fragen für ein bestimmtes Fachgebiet und Modul.
     * @param subjectId ID des Studienfachs
     * @param modulId ID des Moduls
     * @param count Anzahl der gewünschten Fragen
     * @return Liste von QuestionDto
     */
    public List<QuestionDto> getRandomQuestionsForSubjectAndModul(Long subjectId, Long modulId, int count) {
        List<Question> allQuestions = questionRepository.findByStudySubjectIdAndSubjectModulId(subjectId, modulId);

        if (allQuestions.isEmpty()) {
            throw new IllegalStateException("Keine Fragen für dieses Fachgebiet/Modul gefunden.");
        }

        // Durchmischen & nur die gewünschte Anzahl zurückgeben
        Collections.shuffle(allQuestions);

        return allQuestions.stream()
                .limit(count)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<QuestionDto> generateQuestions(Long subjectId, Long modulId, int amount) {
        List<Question> allQuestions = questionRepository.findByStudySubjectIdAndSubjectModulId(subjectId, modulId);

        if (allQuestions.isEmpty()) {
            throw new IllegalStateException("Keine Fragen für dieses Fachgebiet/Modul gefunden.");
        }

        Collections.shuffle(allQuestions);

        return allQuestions.stream()
                .limit(amount)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Konvertiert eine Question-Entity ins DTO.
     */
    private QuestionDto mapToDto(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getQuestionText(),
                question.getAnswers()
        );
    }
}

