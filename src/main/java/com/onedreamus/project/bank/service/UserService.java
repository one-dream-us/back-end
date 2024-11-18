package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.entity.User;
import com.onedreamus.project.bank.repository.UserRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public void join(JoinDto joinDto) {
        if (userRepository.existsByEmail(joinDto.getEmail())) {
            throw new UserException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User newUser = User.from(joinDto);

        userRepository.save(newUser);
    }

}
