package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUserUsernameAndCompletedFalse(String username);
    Optional<GameSession> findByIdAndUserUsername(Long id, String username);
}
