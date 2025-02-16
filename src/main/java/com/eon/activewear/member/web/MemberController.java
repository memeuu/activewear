package com.eon.activewear.member.web;

import com.eon.activewear.member.domain.Gender;
import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
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
    //testcontainers로 통합테스트 test중
    @PostMapping("/signup")
    public String signUp(MemberDTO member) {
        memberService.saveMember(member);
        return "redirect:/";
    }

//    @PostMapping("/signup")
//    public String signUp(MemberDTO member, RedirectAttributes redirectAttributes) {
//        int result = memberService.saveMember(member);
//
//        if (result == 0) {
//            redirectAttributes.addFlashAttribute("duplicateError", "사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
//            return "redirect:/members/signup";
//        }
//        return "redirect:/";
//    }

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

//===================================================================
    // 회원 목록 페이지 이동 추가
    @GetMapping("/listPage")
    public String memberListPage() {
        return "member/list";
    }

    /*
        전체 회원 목록 조회
    */
    @GetMapping("/list")
    @ResponseBody  // JSON 응답으로 반환하도록 추가
    public List<MemberDTO> getMemberList() {
        return memberService.getMemberList();
    }

    /*
        성별 & 이름 검색 기능 추가 (필터링)
    */
    @GetMapping("/filter")
    @ResponseBody
    public List<MemberDTO> getFilteredMembers(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String memName) {
        return memberService.getFilteredMembers(gender, memName);
    }

    /*
        Excel 다운로드
    */
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody Map<String, Object> filterParams,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String gender = (String) filterParams.get("gender");
        String memName = (String) filterParams.get("memName");
        memberService.exportToExcel(gender, memName, request, response);
    }


}//end of class.