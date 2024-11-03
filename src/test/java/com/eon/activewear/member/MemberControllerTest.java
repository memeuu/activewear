package com.eon.activewear.member;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional //각 테스트 메서드 실행된 후 자동으로 롤백
@DisplayName("[MemberController] 통합 테스트")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberDTO member;
    @BeforeEach
    void setUp() {
        member = new MemberDTO();
        member.setId("test11");
        member.setPwd("password123!");
        member.setMemName("테스트");
        member.setPhone("010-1234-5678");
        member.setEmail("test11@naver.com");
    }


// < 회원가입 테스트 >

    //1. 회원가입 기능

    //1-1) 회원가입 성공
    //기본으로 일단 테스트 - 다시 수정하기
    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp() throws Exception {

        MemberDTO newMember = new MemberDTO();
        newMember.setId("controller1");
        newMember.setPwd("password123!");
        newMember.setMemName("통힙테스트");
        newMember.setPhone("010-1234-5679");
        newMember.setEmail("controller1@naver.com");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(newMember);

        mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON) // Spring의 MediaType
                .content(json))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/")) // 리다이렉트 URL 확인
                .andDo(print());

        verify(memberService, times(1)).saveMember(any(MemberDTO.class));
    }

}
