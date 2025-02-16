package com.eon.activewear.member.service.impl;

import com.eon.activewear.member.domain.MemberDTO;
import com.eon.activewear.member.mapper.MemberMapper;
import com.eon.activewear.member.service.MemberService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

//========================================================
    /* 회원 목록 조회 */
    @Override
    public List<MemberDTO> getMemberList() {
        return memberMapper.getAllMembers();
    }

    /* 검색 회원 목록 조회 */
    @Override
    public List<MemberDTO> getFilteredMembers(String gender, String memName) {
        return memberMapper.getFilteredMembers(gender, memName);
    }

    /* Excel 다운로드 */
    @Override
    public void exportToExcel(String gender, String memName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<MemberDTO> filteredMembers = getFilteredMembers(gender, memName);

        log.info("엑셀 다운로드 요청 - 필터링된 회원 수: {}", filteredMembers.size());

        if (filteredMembers.isEmpty()) {
            log.error("엑셀 데이터가 없습니다.");
            throw new Exception("다운로드할 데이터가 없습니다.");
        }

//        SXSSFWorkbook workbook = new SXSSFWorkbook();
        XSSFWorkbook workbook = new XSSFWorkbook(); // 내 컴퓨터 한셀이라 변경
        Sheet sheet = workbook.createSheet("Members");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        String[] columns = {"회원 코드", "아이디", "이름", "전화번호", "이메일", "생년월일", "성별", "주소"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // 데이터 추가 (Null 체크 포함)
        int rowIdx = 1;
        for (MemberDTO member : filteredMembers) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(member.getMemCode());
            row.createCell(1).setCellValue(member.getId());
            row.createCell(2).setCellValue(member.getMemName());
            row.createCell(3).setCellValue(member.getPhone());
            row.createCell(4).setCellValue(member.getEmail());
            row.createCell(5).setCellValue(member.getBirth() != null ? member.getBirth().toString() : "");
            row.createCell(6).setCellValue(member.getGender() != null ? member.getGender().toString() : "");
            row.createCell(7).setCellValue(member.getMemAddress() != null ? member.getMemAddress() : "");
        }

        // 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"members.xlsx\"");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // OutputStream을 한 번만 사용 & 확실하게 닫기
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("엑셀 파일 작성 중 오류 발생: {}", e.getMessage());
        } finally {
            //workbook.dispose();  // SXSSFWorkbook 메모리 정리 // XSSFWorkbook은 dispose() 필요 없음
            workbook.close();    // 파일 닫기
        }

        log.info("엑셀 파일 생성 및 전송 완료.");
    }





}
