package com.eon.activewear.member.mapper;

import com.eon.activewear.member.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MemberMapper {

    public int insertMember(MemberDTO dto);
    public int getSavedId(String memId);

    /* 회원 목록 조회 */
    List<MemberDTO> getAllMembers();

    /* 검색 회원 목록 조회 */
//    List<MemberDTO> getFilteredMembers(@Param("gender") String gender, @Param("memName") String memName);
    List<MemberDTO> getFilteredMembers(String gender, String memName);

}
