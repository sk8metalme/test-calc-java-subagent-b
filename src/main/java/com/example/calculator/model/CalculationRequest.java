package com.example.calculator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 計算リクエストのモデルクラス
 */
public class CalculationRequest {
    
    @NotBlank(message = "式を入力してください")
    @Pattern(regexp = "^[0-9+\\-*/.\\s()]+$", message = "有効な数式を入力してください")
    private String expression;
    
    public CalculationRequest() {}
    
    public CalculationRequest(String expression) {
        this.expression = expression;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    @Override
    public String toString() {
        return "CalculationRequest{" +
                "expression='" + expression + '\'' +
                '}';
    }
}
