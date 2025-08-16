package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Korrekte Nutzung des Feldes subjectModul
    List<Question> findByStudySubjectIdAndSubjectModulId(Long studySubjectId, Long subjectModulId);

    // Für bestehende andere Methoden:
    List<Question> findAllBySubjectModulId(Long subjectModulId);

    @Query("SELECT q FROM Question q WHERE q.subjectModul.id = :modulId")
    List<Question> findAllByModulId(@Param("modulId") Long modulId);

    @Query(value = "SELECT * FROM question WHERE modul_id = :modulId ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Question> findRandomBySubjectModulId(@Param("modulId") Long modulId, @Param("count") int count);
}
