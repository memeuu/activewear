package com.eon.activewear.member.service.impl;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.mapper.MemberMapper;
import com.eon.activewear.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Autowired
    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }


/* < 회원가입 > */
    @Override
    public int saveMember(MemberDTO dto) {
        log.info("save: member= {}", dto);

        //아이디 중복 체크
        if (memberMapper.getSavedId(dto.getId()) > 0) {
            return 0; //중복된 ID일 경우 실패
        }

        //비밀번호 해싱
        String hashPwd = BCrypt.hashpw(dto.getPwd(), BCrypt.gensalt());
        dto.setPwd(hashPwd);

        return memberMapper.insertMember(dto);
    }

    //아이디 중복 확인
    @Override
    public int findSavedId(String memId) {
        return memberMapper.getSavedId(memId);
    }



}
