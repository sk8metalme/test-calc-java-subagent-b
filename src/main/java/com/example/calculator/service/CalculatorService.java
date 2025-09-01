package com.example.calculator.service;

import com.example.calculator.model.CalculationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 電卓サービスクラス
 * 四則演算と基本的な数式計算を提供
 */
@Service
public class CalculatorService {
    
    private static final int SCALE = 10; // 小数点以下の桁数
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    /**
     * 数式を計算する
     * @param expression 計算式
     * @return 計算結果
     */
    public CalculationResult calculate(String expression) {
        try {
            if (expression == null || expression.trim().isEmpty()) {
                return new CalculationResult(expression, "式が入力されていません", false);
            }
            
            // 式を正規化
            String normalizedExpression = normalizeExpression(expression.trim());
            
            // 式の妥当性をチェック
            if (!isValidExpression(normalizedExpression)) {
                return new CalculationResult(expression, "無効な式です", false);
            }
            
            // 計算実行
            BigDecimal result = evaluateExpression(normalizedExpression);
            
            // 結果を文字列に変換
            String resultString = formatResult(result);
            
            return new CalculationResult(expression, resultString);
            
        } catch (ArithmeticException e) {
            return new CalculationResult(expression, "計算エラー: " + e.getMessage(), false);
        } catch (Exception e) {
            return new CalculationResult(expression, "予期しないエラーが発生しました", false);
        }
    }
    
    /**
     * 式を正規化する
     */
    private String normalizeExpression(String expression) {
        // 空白を削除
        expression = expression.replaceAll("\\s+", "");
        
        // 連続する演算子を処理
        expression = expression.replaceAll("\\+\\+", "+");
        expression = expression.replaceAll("--", "+");
        expression = expression.replaceAll("\\+-", "-");
        expression = expression.replaceAll("-\\+", "-");
        
        return expression;
    }
    
    /**
     * 式の妥当性をチェック
     */
    private boolean isValidExpression(String expression) {
        if (expression.isEmpty()) {
            return false;
        }
        
        // 基本的な文字チェック
        if (!expression.matches("^[0-9+\\-*/.\\s()]+$")) {
            return false;
        }
        
        // 括弧の対応をチェック
        int parenthesesCount = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
                if (parenthesesCount < 0) {
                    return false;
                }
            }
        }
        
        if (parenthesesCount != 0) {
            return false;
        }
        
        // 演算子の位置をチェック
        if (expression.matches(".*[+\\-*/]$") || expression.matches("^[+*/].*")) {
            return false;
        }
        
        // 連続する演算子をチェック（ただし、負の数は許可）
        if (expression.matches(".*[+*/]{2,}.*") || expression.matches(".*[+\\-]{2,}.*")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 数式を評価する（逆ポーランド記法を使用）
     */
    private BigDecimal evaluateExpression(String expression) {
        // 中置記法を後置記法に変換
        String postfix = infixToPostfix(expression);
        
        // 後置記法を評価
        return evaluatePostfix(postfix);
    }
    
    /**
     * 中置記法を後置記法に変換
     */
    private String infixToPostfix(String infix) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                // 数値を処理
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    number.append(infix.charAt(i));
                    i++;
                }
                i--; // ループでi++されるので1つ戻す
                result.append(number).append(" ");
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop()).append(" ");
                }
                stack.pop(); // '('を削除
            } else if (precedence.containsKey(c)) {
                while (!stack.isEmpty() && stack.peek() != '(' && 
                       precedence.get(stack.peek()) >= precedence.get(c)) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }
        
        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }
        
        return result.toString().trim();
    }
    
    /**
     * 後置記法を評価
     */
    private BigDecimal evaluatePostfix(String postfix) {
        Stack<BigDecimal> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");
        
        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(new BigDecimal(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new ArithmeticException("演算子に対して十分な数値がありません");
                }
                
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                BigDecimal result = performOperation(a, b, token);
                stack.push(result);
            }
        }
        
        if (stack.size() != 1) {
            throw new ArithmeticException("計算結果が不正です");
        }
        
        return stack.pop();
    }
    
    /**
     * 数値かどうかを判定
     */
    private boolean isNumber(String token) {
        try {
            new BigDecimal(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 演算子かどうかを判定
     */
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
    
    /**
     * 演算を実行
     */
    private BigDecimal performOperation(BigDecimal a, BigDecimal b, String operator) {
        return switch (operator) {
            case "+" -> a.add(b);
            case "-" -> a.subtract(b);
            case "*" -> a.multiply(b);
            case "/" -> {
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("ゼロ除算はできません");
                }
                yield a.divide(b, SCALE, ROUNDING_MODE);
            }
            default -> throw new ArithmeticException("未対応の演算子: " + operator);
        };
    }
    
    /**
     * 結果をフォーマット
     */
    private String formatResult(BigDecimal result) {
        // 小数点以下が0の場合は整数として表示
        if (result.scale() <= 0 || result.stripTrailingZeros().scale() <= 0) {
            return result.setScale(0, ROUNDING_MODE).toString();
        }
        
        // 小数点以下を適切な桁数に調整
        return result.stripTrailingZeros().toPlainString();
    }
}
