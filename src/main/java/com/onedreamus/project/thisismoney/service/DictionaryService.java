package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.repository.DictionaryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public Optional<Dictionary> getDictionaryById(Long dictionaryId){
        return dictionaryRepository.findById(dictionaryId);
    }

}
