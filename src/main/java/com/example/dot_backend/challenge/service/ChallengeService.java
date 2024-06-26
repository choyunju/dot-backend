package com.example.dot_backend.challenge.service;

import com.example.dot_backend.challenge.dto.ChallengeRequestDto;
import com.example.dot_backend.challenge.dto.ChallengeResponseDto;
import com.example.dot_backend.challenge.entity.Challenge;
import com.example.dot_backend.challenge.repository.ChallengeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Transactional
    public Long saveChallenge(ChallengeRequestDto challengeRequestDto) {
        Challenge challenge = challengeRepository.save(challengeRequestDto.toSaveChallenge());
        return challenge.getId();
    }

    @Transactional
    public ChallengeResponseDto findChallengeById(Long id) {
        Challenge challenge = challengeRepository.findChallengeById(id).orElseThrow(() -> new RuntimeException("no challenge"));
        return challenge.getChallengeResponseDto();
    }

    @Transactional
    public List<ChallengeResponseDto> findAllByUserId(Long userId) {
        List<Challenge> challengeList = challengeRepository.findAllByUserId(userId);
        List<ChallengeResponseDto> challengeResponseDtoList = new ArrayList<>();
        for(Challenge challenge : challengeList) {
            challengeResponseDtoList.add(challenge.getChallengeResponseDto());
        }
        return challengeResponseDtoList;
    }

    @Transactional
    public Long modifyChallengeState(Long id, Long count) {
        Challenge challenge = challengeRepository.findChallengeById(id).orElseThrow(() -> new RuntimeException("no challenge"));
        System.out.println(challenge.getId());
        challenge.updateState(count);
        return challenge.getId();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCheckedStatus() {
        List<Challenge> challengeList = challengeRepository.findAll();
        for(Challenge challenge : challengeList) {
            challenge.updateCheckState();
        }
    }

}
