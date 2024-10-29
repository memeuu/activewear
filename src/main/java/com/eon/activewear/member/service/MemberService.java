package com.eon.activewear.member.service;

import com.eon.activewear.member.domain.MemberDTO;

public interface MemberService {

    public int saveMember(MemberDTO dto);
    public int findSavedId(String memId);
}
