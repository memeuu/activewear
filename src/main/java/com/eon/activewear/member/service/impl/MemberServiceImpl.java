package com.eon.activewear.member.service.impl;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.mapper.MemberMapper;
import com.eon.activewear.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
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
        return memberMapper.insertMember(dto);
    }

    //아이디 중복 확인
    @Override
    public int findSavedId(String memId) {
        return memberMapper.getSavedId(memId);
    }



}
