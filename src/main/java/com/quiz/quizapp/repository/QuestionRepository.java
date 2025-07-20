package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySubjectModulId(Long id);
}
