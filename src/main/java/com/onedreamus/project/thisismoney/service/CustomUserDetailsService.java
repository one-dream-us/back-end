package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.exception.UserException;
import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        Users users = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserException(ErrorCode.NO_USER));


        //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
        return new CustomUserDetails(users);

    }
}
