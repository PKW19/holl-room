package com.hollroom.user.controller;

import com.hollroom.user.dto.UserLoginDTO;
import com.hollroom.user.dto.UserSignupDTO;
import com.hollroom.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller //restcontroller는 데이터를 리턴, 뷰를 리턴하려면 controller로 변환
@Slf4j
@RequiredArgsConstructor
public class UserController {
    //================================================================================================================//
    private final UserService userService;
    //================================================================================================================//
    //회원가입 요청
    @PostMapping("/signup")
    public void signup(UserSignupDTO userSignupDTO){
        userService.signup(userSignupDTO);
    }
    //로그인 요청
    @PostMapping("/login")
    public void login(UserLoginDTO userLoginDTO, HttpServletRequest request){
        log.info(userLoginDTO.getUserEmail());
        userService.login(userLoginDTO, request);
    }
    //닉네임 중복 확인 요청
    @GetMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestParam("userNickname") String userNickname){
        try {
            userService.isNicknameDuplicate(userNickname).orElseThrow(() -> new RuntimeException("사용 가능한 닉네임입니다,"));
            return ResponseEntity.ok().body("닉네임이 이미 사용 중입니다.");
        } catch (Exception e){
            return ResponseEntity.ok().body("사용 가능한 닉네임입니다.");
        }
    }
    //이메일 중복 확인
    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@RequestParam("userEmail") String userEmail){
        try {
            userService.isEmailAlreadyExists(userEmail).orElseThrow(() -> new RuntimeException("사용 가능한 이메일입니다."));
            return ResponseEntity.ok().body("이미 사용 중인 이메일입니다.");
        } catch (Exception e){
            return ResponseEntity.ok().body("사용 가능한 이메일입니다.");
        }
    }

    @GetMapping("/checkLogin")
    @ResponseBody
    public ResponseEntity<Boolean> checkLogin(HttpSession session){
        if(session.getAttribute("USER_NICKNAME") != null){
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
            log.info("세션 있음");
            return ResponseEntity.ok("로그아웃 성공");
        } else{
            log.info("세션 없음");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("세션이 없습니다.");
        }
    }
}
