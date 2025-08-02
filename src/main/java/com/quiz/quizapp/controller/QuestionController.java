package com.quiz.quizapp.controller;

import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.entity.Question;
import com.quiz.quizapp.entity.StudySubject;
import com.quiz.quizapp.entity.SubjectModul;
import com.quiz.quizapp.repository.QuestionRepository;
import com.quiz.quizapp.repository.StudySubjectRepository;
import com.quiz.quizapp.repository.SubjectModulRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionRepository questionRepository;

    private final StudySubjectRepository studySubjectRepository;

    private final SubjectModulRepository subjectModulRepository;

    public QuestionController(QuestionRepository questionRepository, StudySubjectRepository studySubjectRepository, SubjectModulRepository subjectModulRepository) {
        this.questionRepository = questionRepository;
        this.studySubjectRepository = studySubjectRepository;
        this.subjectModulRepository = subjectModulRepository;
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDto dto) {
        Optional<StudySubject> subjectOpt = studySubjectRepository.findById(dto.getStudySubjectId());
        Optional<SubjectModul> modulOpt = subjectModulRepository.findById(dto.getSubjectModulId()
        );

        if (subjectOpt.isEmpty() || modulOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Falsches Studienfach oder Modul");
        }

        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setAnswers(dto.getAnswers());
        question.setCorrectAnswerIndex(dto.getCorrectAnswerIndex());
        question.setStudySubject(subjectOpt.get());
        question.setSubjectModul(modulOpt.get());

        questionRepository.save(question);

        return ResponseEntity.ok("Frage gespeichert");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuestions(
            @RequestParam Long subjectModulId,
            @RequestParam(defaultValue = "10") int amount
    ) {
        List<Question> all = questionRepository.findAllByModulId(subjectModulId);
        Collections.shuffle(all);
        List<QuestionDto> result = all.stream()
                .limit(amount)
                .map(QuestionDto::fromEntity)
                .toList();

        return ResponseEntity.ok(result);
    }

}
