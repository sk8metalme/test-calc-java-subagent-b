package com.example.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホームコントローラー
 */
@Controller
public class HomeController {
    
    /**
     * ルートパスで電卓ページにリダイレクト
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/calculator";
    }
}
