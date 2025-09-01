package com.example.calculator.controller;

import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import com.example.calculator.service.CalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CalculatorControllerのテストクラス
 */
@WebMvcTest(controllers = CalculatorController.class)
class CalculatorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CalculatorService calculatorService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        // デフォルトのモック設定
        when(calculatorService.calculate(anyString())).thenReturn(new CalculationResult("2 + 3", "5"));
    }
    
    @Test
    @DisplayName("電卓ページの表示テスト")
    void testCalculatorPage() throws Exception {
        mockMvc.perform(get("/calculator"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculationRequest"))
                .andExpect(model().attributeExists("history"));
    }
    
    @Test
    @DisplayName("正常な計算リクエストのテスト")
    void testValidCalculation() throws Exception {
        CalculationResult expectedResult = new CalculationResult("2 + 3", "5");
        when(calculatorService.calculate("2 + 3")).thenReturn(expectedResult);
        
        mockMvc.perform(post("/calculator/calculate")
                .param("expression", "2 + 3"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculationRequest"))
                .andExpect(model().attributeExists("calculationResult"))
                .andExpect(model().attributeExists("history"));
    }
    
    @Test
    @DisplayName("無効な計算リクエストのテスト")
    void testInvalidCalculation() throws Exception {
        CalculationResult errorResult = new CalculationResult("invalid", "無効な式です", false);
        when(calculatorService.calculate("invalid")).thenReturn(errorResult);
        
        mockMvc.perform(post("/calculator/calculate")
                .param("expression", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculationRequest"))
                .andExpect(model().attributeExists("calculationResult"))
                .andExpect(model().attributeExists("history"));
    }
    
    @Test
    @DisplayName("履歴クリアのテスト")
    void testClearHistory() throws Exception {
        mockMvc.perform(post("/calculator/clear-history"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculationRequest"))
                .andExpect(model().attributeExists("history"));
    }
    
    @Test
    @DisplayName("API計算エンドポイントのテスト")
    void testCalculateApi() throws Exception {
        CalculationResult expectedResult = new CalculationResult("2 + 3", "5");
        when(calculatorService.calculate("2 + 3")).thenReturn(expectedResult);
        
        CalculationRequest request = new CalculationRequest("2 + 3");
        
        mockMvc.perform(post("/calculator/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expression").value("2 + 3"))
                .andExpect(jsonPath("$.result").value("5"))
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    @DisplayName("API計算エンドポイント - エラーケース")
    void testCalculateApiError() throws Exception {
        CalculationResult errorResult = new CalculationResult("invalid", "無効な式です", false);
        when(calculatorService.calculate("invalid")).thenReturn(errorResult);
        
        CalculationRequest request = new CalculationRequest("invalid");
        
        mockMvc.perform(post("/calculator/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expression").value("invalid"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("無効な式です"));
    }
    
    @Test
    @DisplayName("バリデーションエラーのテスト")
    void testValidationError() throws Exception {
        mockMvc.perform(post("/calculator/calculate")
                .param("expression", "")) // 空の式
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculationRequest"));
    }
    
    @Test
    @DisplayName("無効なJSONリクエストのテスト")
    void testInvalidJsonRequest() throws Exception {
        mockMvc.perform(post("/calculator/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }
}
