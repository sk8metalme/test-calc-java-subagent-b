package com.example.calculator.service;

import com.example.calculator.model.CalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalculatorServiceのテストクラス
 */
class CalculatorServiceTest {
    
    private CalculatorService calculatorService;
    
    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }
    
    @Test
    @DisplayName("基本的な加算テスト")
    void testBasicAddition() {
        CalculationResult result = calculatorService.calculate("2 + 3");
        assertTrue(result.isSuccess());
        assertEquals("5", result.getResult());
    }
    
    @Test
    @DisplayName("基本的な減算テスト")
    void testBasicSubtraction() {
        CalculationResult result = calculatorService.calculate("5 - 3");
        assertTrue(result.isSuccess());
        assertEquals("2", result.getResult());
    }
    
    @Test
    @DisplayName("基本的な乗算テスト")
    void testBasicMultiplication() {
        CalculationResult result = calculatorService.calculate("4 * 3");
        assertTrue(result.isSuccess());
        assertEquals("12", result.getResult());
    }
    
    @Test
    @DisplayName("基本的な除算テスト")
    void testBasicDivision() {
        CalculationResult result = calculatorService.calculate("10 / 2");
        assertTrue(result.isSuccess());
        assertEquals("5", result.getResult());
    }
    
    @Test
    @DisplayName("複雑な計算テスト")
    void testComplexCalculation() {
        CalculationResult result = calculatorService.calculate("2 + 3 * 4");
        assertTrue(result.isSuccess());
        assertEquals("14", result.getResult());
    }
    
    @Test
    @DisplayName("括弧を含む計算テスト")
    void testCalculationWithParentheses() {
        CalculationResult result = calculatorService.calculate("(2 + 3) * 4");
        assertTrue(result.isSuccess());
        assertEquals("20", result.getResult());
    }
    
    @Test
    @DisplayName("小数点を含む計算テスト")
    void testDecimalCalculation() {
        CalculationResult result = calculatorService.calculate("3.5 + 2.5");
        assertTrue(result.isSuccess());
        assertEquals("6", result.getResult());
    }
    
    @Test
    @DisplayName("ゼロ除算エラーテスト")
    void testDivisionByZero() {
        CalculationResult result = calculatorService.calculate("5 / 0");
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("ゼロ除算"));
    }
    
    @Test
    @DisplayName("空の式エラーテスト")
    void testEmptyExpression() {
        CalculationResult result = calculatorService.calculate("");
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("式が入力されていません"));
    }
    
    @Test
    @DisplayName("null式エラーテスト")
    void testNullExpression() {
        CalculationResult result = calculatorService.calculate(null);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("式が入力されていません"));
    }
    
    @Test
    @DisplayName("無効な式エラーテスト")
    void testInvalidExpression() {
        CalculationResult result = calculatorService.calculate("2 + + 3");
        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessage());
    }
    
    @Test
    @DisplayName("括弧の不一致エラーテスト")
    void testMismatchedParentheses() {
        CalculationResult result = calculatorService.calculate("(2 + 3");
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("無効な式"));
    }
    
    @ParameterizedTest
    @CsvSource({
        "1 + 1, 2",
        "10 - 5, 5",
        "3 * 4, 12",
        "15 / 3, 5",
        "2 + 3 * 4, 14",
        "(2 + 3) * 4, 20",
        "10 / 2 + 3, 8",
        "2 * (3 + 4), 14"
    })
    @DisplayName("パラメータ化テスト - 様々な計算")
    void testVariousCalculations(String expression, String expectedResult) {
        CalculationResult result = calculatorService.calculate(expression);
        assertTrue(result.isSuccess(), "計算が成功するはず: " + expression);
        assertEquals(expectedResult, result.getResult(), "計算結果が一致するはず: " + expression);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "abc",
        "2 + abc",
        "2 @ 3",
        "2 +",
        "+ 2",
        "2 + + 3",
        "2..3",
        "2 + 3 +"
    })
    @DisplayName("パラメータ化テスト - 無効な式")
    void testInvalidExpressions(String expression) {
        CalculationResult result = calculatorService.calculate(expression);
        assertFalse(result.isSuccess(), "無効な式は失敗するはず: " + expression);
        assertNotNull(result.getErrorMessage(), "エラーメッセージが存在するはず: " + expression);
    }
    
    @Test
    @DisplayName("大きな数値の計算テスト")
    void testLargeNumberCalculation() {
        CalculationResult result = calculatorService.calculate("999999 + 1");
        assertTrue(result.isSuccess());
        assertEquals("1000000", result.getResult());
    }
    
    @Test
    @DisplayName("負の数値の計算テスト")
    void testNegativeNumberCalculation() {
        CalculationResult result = calculatorService.calculate("5 - 8");
        assertTrue(result.isSuccess());
        assertEquals("-3", result.getResult());
    }
    
    @Test
    @DisplayName("小数点の精度テスト")
    void testDecimalPrecision() {
        CalculationResult result = calculatorService.calculate("1 / 3");
        assertTrue(result.isSuccess());
        // 小数点以下10桁まで表示されることを確認
        assertTrue(result.getResult().length() > 3);
    }
}
