package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySubjectModulId(Long id);

    @Query("SELECT q FROM Question q WHERE q.subjectModul.id = :modulId")
    List<Question> findAllByModulId(@Param("modulId") Long modulId);

    @Query(value = "SELECT * FROM question WHERE subject_modul_id = :modulId ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomBySubjectModulId(@Param("modulId") Long modulId, @Param("count") int count);


}
