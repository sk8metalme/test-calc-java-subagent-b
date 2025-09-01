package com.example.calculator.controller;

import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import com.example.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 電卓コントローラー
 */
@Controller
@RequestMapping("/calculator")
public class CalculatorController {
    
    @Autowired
    private CalculatorService calculatorService;
    
    /**
     * 電卓ページを表示
     */
    @GetMapping
    public String calculator(Model model) {
        model.addAttribute("calculationRequest", new CalculationRequest());
        model.addAttribute("calculationResult", null);
        model.addAttribute("history", new ArrayList<CalculationResult>());
        return "calculator";
    }
    
    /**
     * 計算を実行
     */
    @PostMapping("/calculate")
    public String calculate(@Valid @ModelAttribute CalculationRequest calculationRequest,
                           BindingResult bindingResult,
                           Model model,
                           @SessionAttribute(value = "history", required = false) List<CalculationResult> history) {
        
        if (history == null) {
            history = new ArrayList<>();
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("calculationResult", null);
            model.addAttribute("history", history);
            return "calculator";
        }
        
        CalculationResult result = calculatorService.calculate(calculationRequest.getExpression());
        
        // 履歴に追加（最大10件まで）
        history.add(0, result);
        if (history.size() > 10) {
            history = history.subList(0, 10);
        }
        
        model.addAttribute("calculationRequest", calculationRequest);
        model.addAttribute("calculationResult", result);
        model.addAttribute("history", history);
        
        return "calculator";
    }
    
    /**
     * 履歴をクリア
     */
    @PostMapping("/clear-history")
    public String clearHistory(Model model) {
        model.addAttribute("calculationRequest", new CalculationRequest());
        model.addAttribute("calculationResult", null);
        model.addAttribute("history", new ArrayList<CalculationResult>());
        return "calculator";
    }
    
    /**
     * 計算API（JSON形式）
     */
    @PostMapping("/api/calculate")
    @ResponseBody
    public CalculationResult calculateApi(@Valid @RequestBody CalculationRequest calculationRequest) {
        return calculatorService.calculate(calculationRequest.getExpression());
    }
}
