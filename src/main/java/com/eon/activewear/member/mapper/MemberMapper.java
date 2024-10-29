package com.eon.activewear.member.mapper;

import com.eon.activewear.member.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberMapper {

    public int insertMember(MemberDTO dto);
    public int getSavedId(String memId);

}
