package com.eon.activewear;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class PageController {

    //메인
    @GetMapping("/")
    public String home() {
        return "common/main";
    }

    //상품목록
    @GetMapping("/list")
    public String list() {
        return "product/list";
    }


}
