package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long>{
}
