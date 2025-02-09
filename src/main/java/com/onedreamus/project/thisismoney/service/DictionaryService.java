package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.DictionaryQuiz;
import com.onedreamus.project.thisismoney.model.dto.DictionaryResponse;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.DictionaryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    // 모든 용어의 ID 관리
    private final Set<Long> dictionaryIdRepository = new HashSet<>();
    private final DictionaryRepository dictionaryRepository;

    @PostConstruct
    private void init() {
        dictionaryIdRepository.addAll(dictionaryRepository.findAllId());
    }

    public Optional<Dictionary> getDictionaryById(Long dictionaryId){
        return dictionaryRepository.findById(dictionaryId);
    }

    public Long getMaxId() {
        return dictionaryRepository.findMaxId();
    }

    public List<Dictionary> getDictionaryList(List<Long> randomNumList) {
        return dictionaryRepository.findByIdIn(randomNumList);
    }

    public boolean contains(long idx) {
        return dictionaryIdRepository.contains(idx);
    }

    public Dictionary saveNewDictionary(Dictionary newDictionary) {
        return dictionaryRepository.save(newDictionary);
    }

    public List<DictionaryResponse> searchDictionary(String keyword) {
        return dictionaryRepository.findByTermContaining(keyword).stream()
            .map(DictionaryResponse::from)
            .toList();
    }
}
