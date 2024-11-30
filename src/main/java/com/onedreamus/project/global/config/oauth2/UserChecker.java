package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.bank.model.dto.UserCheckDto;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserChecker {

    // key: email, value: UserCheckDto
    private final ConcurrentHashMap<String, UserCheckDto> userChecker = new ConcurrentHashMap<>();


    public void addEmail(String email, UserCheckDto userCheckDto){
        userChecker.put(email, userCheckDto);
    }

    public boolean isUser(String email) {
        return userChecker.get(email).isUser();
    }

    public void deleteEmail(String email) {
        userChecker.remove(email);
    }

    public Long getSocialId(String email){
        return userChecker.get(email).getSocialId();
    }
}
