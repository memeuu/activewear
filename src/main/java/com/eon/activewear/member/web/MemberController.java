package com.eon.activewear.member.web;

import com.eon.activewear.member.domain.Gender;
import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /* < 회원가입 > */

    //등록&수정 폼 이동
    @GetMapping("/signup")
    public String signUpPage(@ModelAttribute("member") MemberDTO member) { //수정 폼으로도 사용할 거라 빈 객체 생성해서 넘기기
        member.setGender(Gender.선택안함); // 기본값 설정
        return "member/signUp";
    }

    //회원가입 기능
    @PostMapping("/signup")
    public String signUp(MemberDTO member) {
        memberService.saveMember(member);
        return "redirect:/";
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // templates/index.html을 반환
    }

    //아이디 중복 체크 ( AJAX 요청 )
    @GetMapping("/checkId")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam String memId) {
        Map<String, Boolean> response = new HashMap<>();

        int count = memberService.findSavedId(memId);
        response.put("isDuplicate", count != 0);
        return ResponseEntity.ok(response); // 항상 200 OK
    }
    
    //성별 Gender Enum 타입
    @ModelAttribute("genderTypes")
    public Gender[] genderTypes() {
        return Gender.values();
    }





}//end of class.