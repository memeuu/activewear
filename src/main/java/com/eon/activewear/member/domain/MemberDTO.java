package com.eon.activewear.member.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
    Bean Validation 기능
    - @NotEmpty: 컬렉션 타입에 대한 검증으로, 컬렉션이 비어있지 않은지 확인
    - @NotNull: 객체에 대한 검증으로, null 값만 체크
    - @NotBlank: 문자열에 대한 검증으로, null / 빈 문자열 "" / 공백만 있는 문자열 " " 모두 체크
*/

@Getter
@Setter
public class MemberDTO {

    private Long memCode; //PK (auto-increment)
    private String id;
    private String pwd;
    private String memName;
    private String phone;
    private String email;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date birth;
    private Gender gender;
    private String memPostalCode;
    private String memAddress;
    private String memAddressDetail;

    private int termsAgreed; //약관동의 여부
    private int isVerified; //이메일인증 여부

}
