package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.StudySubject;
import com.quiz.quizapp.entity.SubjectModul;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudySubjectRepository extends JpaRepository<StudySubject, Long> {

    // Gibt die Module für ein bestimmtes StudySubject zurück
    @Query("SELECT m FROM StudySubject s JOIN s.modules m WHERE s.id = :subjectId")
    List<SubjectModul> findModulesByStudySubjectId(@Param("subjectId") Long subjectId);

}
