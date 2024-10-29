package com.eon.activewear.member;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.mapper.MemberMapper;
import com.eon.activewear.member.service.MemberService;
import com.eon.activewear.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("[MemberService] 단위 테스트")
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper; //실제 구현 대신 Mock 객체 사용

    @InjectMocks
    private MemberServiceImpl memberService; //테스트 대상

    @Test
    void save() {
        //given
        MemberDTO member = new MemberDTO();
        member.setId("test11");
        member.setPwd("t123123!");
        member.setMemName("테스트");
        member.setPhone("01011112222");

        when(memberMapper.getSavedId(member.getId())).thenReturn(0); // 중복 없음
        when(memberMapper.insertMember(any(MemberDTO.class))).thenReturn(1); // 가입 성공

        //when
        int savedOK = memberService.saveMember(member); //가입

        //then
        assertThat(savedOK).isEqualTo(1); //성공 확인

        //아이디 중복체크
        when(memberMapper.getSavedId(member.getId())).thenReturn(1);
        int duplicatedId = memberService.findSavedId(member.getId());

        assertThat(duplicatedId).isEqualTo(1);
    }

}
