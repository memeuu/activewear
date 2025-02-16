package com.eon.activewear.member.service;

import com.eon.activewear.member.domain.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MemberService {

    public int saveMember(MemberDTO dto);
    public int findSavedId(String memId);

    /* 회원 목록 조회 */
    List<MemberDTO> getMemberList();

    /* 검색 회원 목록 조회 */
    List<MemberDTO> getFilteredMembers(String gender, String memName);

    /* Excel 다운로드 */
    void exportToExcel(String gender, String memName, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
