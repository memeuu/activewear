package com.eon.activewear.member;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.mapper.MemberMapper;
import com.eon.activewear.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("[MemberService] 단위 테스트")
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper; //Mock 객체 사용 (실제 DB 접근X)

    @InjectMocks
    private MemberServiceImpl memberService; //테스트할 Service 클래스

    private MemberDTO member;
    @BeforeEach
    void setUp() {
        member = new MemberDTO();
        member.setId("test11");
        member.setPwd("password123!");
    }


// < 회원가입 메서드 saveMember 테스트 >

    //1. 회원가입 성공 test
    @Test
    @DisplayName("회원가입 성공 테스트")
    void save_success() {
        // given (초기화된 member 사용)
        // 스텁 설정
        given(memberMapper.getSavedId(member.getId())).willReturn(0); // 중복된 아이디 X
        given(memberMapper.insertMember(any(MemberDTO.class))).willReturn(1); // 가입 성공

        // when
        int result = memberService.saveMember(member); // 회원가입

        // then
        assertThat(result).isEqualTo(1); // 회원가입 성공 확인
        verify(memberMapper).insertMember(argThat(savedMember ->
                BCrypt.checkpw("password123!", savedMember.getPwd()) // 비밀번호 해시 검증
        ));
    }

    //2. 회원가입 실패 test
    @Test
    @DisplayName("회원가입 실패 테스트")
    void save_fail() {
        // given (초기화된 member 사용)
        // 스텁 설정
        given(memberMapper.getSavedId(member.getId())).willReturn(1); // 중복된 아이디 O

        // when
        int result = memberService.saveMember(member); // 회원가입

        // then
        assertThat(result).isEqualTo(0); // 회원가입 실패 확인
        verify(memberMapper, never()).insertMember(any()); // insertMember 호출되지 않아야 함! (스텁)
    }


// < 아이디 중복체크 findSavedId 테스트 >

    //1. 존재하는 아이디
    @Test
    @DisplayName("중복 아이디 O 테스트")
    void findSavedId_existing() {
        // given (초기화된 member 사용)
        String memId = member.getId();
        //스텁 설정
        given(memberMapper.getSavedId(memId)).willReturn(1);

        //when
        int savedId = memberService.findSavedId(memId);

        //then
        assertThat(savedId).isEqualTo(1); // 값 검증
        verify(memberMapper).getSavedId(memId); // 호출 확인
    }

    //2. 존재하지 않는 아이디
    @Test
    @DisplayName("중복 아이디 X 테스트")
    void findSavedId_notExisting() {
        // given (초기화된 member 사용)
        String memId = member.getId();
        //스텁 설정
        given(memberMapper.getSavedId(memId)).willReturn(0);

        //when
        int savedId = memberService.findSavedId(memId);

        //then
        assertThat(savedId).isEqualTo(0); // 값 검증
        verify(memberMapper).getSavedId(memId); // 호출 확인
    }

}
