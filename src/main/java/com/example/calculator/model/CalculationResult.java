package com.example.calculator.model;

/**
 * 計算結果のモデルクラス
 */
public class CalculationResult {
    
    private String expression;
    private String result;
    private boolean success;
    private String errorMessage;
    
    public CalculationResult() {}
    
    public CalculationResult(String expression, String result) {
        this.expression = expression;
        this.result = result;
        this.success = true;
    }
    
    public CalculationResult(String expression, String errorMessage, boolean success) {
        this.expression = expression;
        this.errorMessage = errorMessage;
        this.success = success;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String toString() {
        return "CalculationResult{" +
                "expression='" + expression + '\'' +
                ", result='" + result + '\'' +
                ", success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
