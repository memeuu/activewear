package com.eon.activewear.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class PageController {

    //메인
    @GetMapping("/")
    public String home() {
        return "index";
    }

    //상품목록
    @GetMapping("/list")
    public String list() {
        return "pages/list";
    }
    


}
