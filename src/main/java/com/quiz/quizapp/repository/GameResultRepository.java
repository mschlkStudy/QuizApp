package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.GameResult;
import com.quiz.quizapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByUser(User user);
}

