package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.CoopSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoopSessionRepository extends JpaRepository<CoopSession, Long> {
    List<CoopSession> findByStatus(CoopSession.CoopStatus status);
    Optional<CoopSession> findBySubjectIdAndModulIdAndStatus(Long subjectId, Long modulId, CoopSession.CoopStatus status);
}



