package com.quiz.quizapp.repository;

import com.quiz.quizapp.entity.StudySubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySubjectRepository extends JpaRepository<StudySubject, Long> {
}
