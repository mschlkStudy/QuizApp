package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.DuelSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DuelSessionRepository extends JpaRepository<DuelSession, Long> {

    Optional<DuelSession> findFirstBySubjectIdAndModulIdAndStatus(
            Long subjectId,
            Long modulId,
            DuelSession.DuelStatus status
    );

    Optional<DuelSession> findById(Long duelId);

    List<DuelSession> findAllByStatus(DuelSession.DuelStatus status);

    @Query("""
        SELECT d FROM DuelSession d
        WHERE d.status = :status
          AND (d.player1 = :username OR d.player2 = :username)
    """)
    List<DuelSession> findAllByStatusAndPlayer(
            @Param("username") String username,
            @Param("status") DuelSession.DuelStatus status
    );

    @Query("""
        SELECT d FROM DuelSession d
        WHERE d.status IN :statuses
          AND (d.player1 = :username OR d.player2 = :username)
    """)
    List<DuelSession> findAllByStatusesAndPlayer(
            @Param("username") String username,
            @Param("statuses") List<DuelSession.DuelStatus> statuses
    );

}
