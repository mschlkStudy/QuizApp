package com.quiz.quizapp.service;

import com.quiz.quizapp.dto.CompletedDuelDto;
import com.quiz.quizapp.dto.DuelSessionDto;
import com.quiz.quizapp.dto.QuestionDto;
import com.quiz.quizapp.entity.DuelSession;
import com.quiz.quizapp.entity.DuelSession.DuelStatus;
import com.quiz.quizapp.entity.Question;
import com.quiz.quizapp.repository.DuelSessionRepository;
import com.quiz.quizapp.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DuelService {

    private final DuelSessionRepository duelSessionRepository;
    private final QuestionRepository questionRepo;
    public DuelService(DuelSessionRepository duelSessionRepository, QuestionRepository questionRepo) {
        this.duelSessionRepository = duelSessionRepository;
        this.questionRepo = questionRepo;
    }

    @Transactional
    public DuelSessionDto startOrJoin(Long subjectId, Long modulId, int amount, String username, String subjectName, String modulName) {
        Optional<DuelSession> openSessionOpt = duelSessionRepository
                .findFirstBySubjectIdAndModulIdAndStatus(subjectId, modulId, DuelStatus.WAITING_FOR_PLAYER);

        if (openSessionOpt.isPresent() && !openSessionOpt.get().getPlayer1().equals(username)) {
            DuelSession openSession = openSessionOpt.get();
            openSession.setPlayer2(username);
            openSession.setStatus(DuelStatus.IN_PROGRESS);
            duelSessionRepository.save(openSession);
            return mapToDto(openSession, username);
        }

        List<Question> questions =  questionRepo.findRandomBySubjectModulId(modulId, amount);
        if (questions.isEmpty()) {
            throw new RuntimeException("Keine Fragen gefunden");
        }



        DuelSession session = new DuelSession();
        session.setPlayer1(username);
        session.setSubjectId(subjectId);
        session.setModulId(modulId);
        session.setQuestions(questions);
        session.setStatus(DuelStatus.WAITING_FOR_PLAYER);
        session.setPlayer1Index(0);
        session.setPlayer1Score(0);
        session.setModulName(modulName);
        session.setSubjectName(subjectName);
        duelSessionRepository.save(session);

        return mapToDto(session, username);
    }

    @Transactional
    public void updateProgress(Long duelId, String username, int index, int score) {
        DuelSession session = duelSessionRepository.findById(duelId)
                .orElseThrow(() -> new RuntimeException("Duel not found"));

        if (username.equals(session.getPlayer1())) {
            session.setPlayer1Index(index);
            session.setPlayer1Score(score);
        } else if (username.equals(session.getPlayer2())) {
            session.setPlayer2Index(index);
            session.setPlayer2Score(score);
        }
        duelSessionRepository.save(session);
    }

    @Transactional
    public void completeDuel(Long duelId, String username) {
        DuelSession session = duelSessionRepository.findById(duelId)
                .orElseThrow(() -> new RuntimeException("Duel not found"));

        if (username.equals(session.getPlayer1())) {
            session.setPlayer1Finished(true);
        } else if (username.equals(session.getPlayer2())) {
            session.setPlayer2Finished(true);
        }

        // Wenn beide fertig → Status auf COMPLETED + Gewinner bestimmen
        if (Boolean.TRUE.equals(session.getPlayer1Finished()) && Boolean.TRUE.equals(session.getPlayer2Finished())) {
            session.setStatus(DuelStatus.COMPLETED);

            if (session.getPlayer1Score() > session.getPlayer2Score()) {
                session.setWinner(session.getPlayer1());
            } else if (session.getPlayer2Score() > session.getPlayer1Score()) {
                session.setWinner(session.getPlayer2());
            } else {
                session.setWinner("DRAW");
            }
        }

        duelSessionRepository.save(session);
    }

    public CompletedDuelDto getDuelWithOpponentScore(Long duelId, String username) {
        DuelSession session = duelSessionRepository.findById(duelId)
                .orElseThrow(() -> new EntityNotFoundException("Duel session not found"));

        // Spielername (nicht User-Objekt!) vergleichen
        String currentUsername = username;

        // Score für aktuellen Spieler
        Integer myScore;
        Integer opponentScore;
        if (currentUsername.equals(session.getPlayer1())) {
            myScore = session.getPlayer1Score();
            opponentScore = session.getPlayer2Score();
        } else if (currentUsername.equals(session.getPlayer2())) {
            myScore = session.getPlayer2Score();
            opponentScore = session.getPlayer1Score();
        } else {
            throw new IllegalArgumentException("Benutzer ist kein Teilnehmer dieses Duells!");
        }

        // Null abfangen - standardmäßig auf 0 setzen
        if (myScore == null) myScore = 0;
        if (opponentScore == null) opponentScore = 0;

        CompletedDuelDto dto = new CompletedDuelDto();
        dto.setDuelId(session.getId());
        dto.setPlayer1Score(myScore);
        dto.setPlayer2Score(opponentScore);
        dto.setStatus(session.getStatus().toString());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<DuelSession> getOpenDuels(String username) {
        List<DuelSession.DuelStatus> openStatuses = List.of(DuelSession.DuelStatus.IN_PROGRESS, DuelSession.DuelStatus.WAITING_FOR_PLAYER);
        return duelSessionRepository.findAllByStatusesAndPlayer(username, openStatuses);
    }


    @Transactional(readOnly = true)
    public List<DuelSession> getCompletedDuels(String username) {
        return duelSessionRepository.findAllByStatusAndPlayer(username, DuelStatus.COMPLETED);
    }

    @Transactional(readOnly = true)
    public DuelSessionDto getDuelById(Long duelId, String username) {
        DuelSession duelSession = duelSessionRepository.findById(duelId)
                .orElseThrow(() -> new EntityNotFoundException("Duel session not found"));
        return mapToDto(duelSession, username);
    }

    public DuelSessionDto mapToDto(DuelSession session, String username) {
        List<QuestionDto> questionDtos = new ArrayList<>();
        Long id = session.getId();
        String player1 = session.getPlayer1();
        String player2 = session.getPlayer2();
        String status = session.getStatus().name();
        Integer currentQuestionIndex = 0;
        Integer score = 0;
        if (username.equals(player1)) {
            currentQuestionIndex = session.getPlayer1Index();
            score = session.getPlayer1Score();
        }
        if (username.equals(player2)) {
            currentQuestionIndex = session.getPlayer2Index();
            score = session.getPlayer2Score();
        }

        String subjectName = session.getSubjectName();
        String modulName = session.getModulName();
        LocalDateTime startedAt = session.getStartedAt();
        for (Question question : session.getQuestions()) {
            QuestionDto dto = QuestionDto.fromEntity(question);
            questionDtos.add(dto);
        }


        return new DuelSessionDto(id, player1, player2, status, questionDtos, currentQuestionIndex, score, startedAt, subjectName, modulName);
    }

    public CompletedDuelDto mapToCompletedDto(DuelSession session, String currentUser) {
        Long id = session.getId();
        String player1 = session.getPlayer1();
        String player2 = session.getPlayer2();
        Integer player1Score = session.getPlayer1Score();
        Integer player2Score = session.getPlayer2Score();
        String status = session.getStatus().name();
        String winner = session.getWinner();
        String subjectName = session.getSubjectName();
        String modulName = session.getModulName();
        Long subjectId = session.getSubjectId();
        Long modulId = session.getModulId();

      return new CompletedDuelDto(id, player1, player1Score, player2, player2Score, subjectName, modulName, status, winner, subjectId, modulId);
    }
}
