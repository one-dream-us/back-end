//package com.onedreamus.project.global;
//
//import com.onedreamus.project.thisismoney.model.entity.Users;
//import com.onedreamus.project.thisismoney.repository.UserRepository;
//import com.onedreamus.project.global.config.jwt.JWTUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class TestUserInitializer implements CommandLineRunner {
//
//    //TODO: CORS 문제 해결 후 TestUserInitializer 삭제.
//
//    private final UserRepository userRepository;
//    private final JWTUtil jwtUtil;
//
//    @Override
//    public void run(String... args) throws Exception {
//        Optional<Users> userOptional = userRepository.findByEmail("test@naver.com");
//        String token;
//
//        if (userOptional.isEmpty()) {
//            Users user = Users.builder()
//                    .name("testUser")
//                    .role("ROLE_USER")
//                    .provider("kakao")
//                    .deleted(false)
//                    .nickname("test nickname")
//                    .email("test@naver.com")
//                    .build();
//
//            userRepository.save(user);
//
////            token = jwtUtil.createJwt(user.getName(), user.getEmail(), user.getRole(), true);
//        }
////        else{
////            Users user = userOptional.get();
////            token = jwtUtil.createJwt(user.getName(), user.getEmail(), user.getRole(), true);
////        }
////
////
////
////        log.info("!!! Test user JWT >>> {}", token);
//    }
//}
