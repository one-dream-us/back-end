package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.thisismoney.model.dto.UserCheckDto;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UserChecker {

    // key: email, value: UserCheckDto
    private final ConcurrentHashMap<String, UserCheckDto> userChecker = new ConcurrentHashMap<>();

    // 나이 확인 모달에 체크하지 않고 종료하는 경우도 있으므로 특정 시간이 지나도 응답이 없으면 자동으로 unlink 보내기.
    // 스케줄러로 만료된 사용자 정보에 대해서 unlink 요청 보내기

    public void add(String email, UserCheckDto userCheckDto){
        userChecker.put(email, userCheckDto);
    }

    public UserCheckDto get(String email){
        return userChecker.get(email);
    }

    public void delete(String email) {
        userChecker.remove(email);
    }

    public Long getSocialId(String email){
        return userChecker.get(email).getSocialId();
    }
}
