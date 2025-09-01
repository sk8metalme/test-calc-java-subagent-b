package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * アプリケーションの統合テスト
 */
@SpringBootTest
@ActiveProfiles("test")
class CalculatorApplicationTests {

    @Test
    void contextLoads() {
        // Springコンテキストが正常に読み込まれることを確認
    }
}
