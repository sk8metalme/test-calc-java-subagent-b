// 電卓のJavaScript機能

let currentExpression = '';

// ページ読み込み時の初期化
document.addEventListener('DOMContentLoaded', function() {
    const expressionInput = document.getElementById('expressionInput');
    if (expressionInput) {
        currentExpression = expressionInput.value || '';
        updateDisplay();
    }
});

// 式に文字を追加
function appendToExpression(value) {
    // 入力制限
    if (currentExpression.length >= 50) {
        alert('式が長すぎます（最大50文字）');
        return;
    }
    
    // 演算子の連続入力を制限
    if (isOperator(value) && isOperator(getLastChar(currentExpression))) {
        return;
    }
    
    // 小数点の重複を制限
    if (value === '.' && hasConsecutiveDecimal(currentExpression)) {
        return;
    }
    
    currentExpression += value;
    updateDisplay();
}

// 全てクリア
function clearAll() {
    currentExpression = '';
    updateDisplay();
}

// 最後の文字を削除
function backspace() {
    if (currentExpression.length > 0) {
        currentExpression = currentExpression.slice(0, -1);
        updateDisplay();
    }
}

// ディスプレイを更新
function updateDisplay() {
    const expressionElement = document.querySelector('.expression');
    const expressionInput = document.getElementById('expressionInput');
    
    if (expressionElement) {
        expressionElement.textContent = currentExpression || '0';
    }
    
    if (expressionInput) {
        expressionInput.value = currentExpression;
    }
}

// 演算子かどうかを判定
function isOperator(char) {
    return ['+', '-', '*', '/', '(', ')'].includes(char);
}

// 文字列の最後の文字を取得
function getLastChar(str) {
    return str.length > 0 ? str[str.length - 1] : '';
}

// 連続する小数点があるかチェック
function hasConsecutiveDecimal(expression) {
    const parts = expression.split(/[+\-*/()]/);
    const lastPart = parts[parts.length - 1];
    return lastPart.includes('.');
}

// キーボード入力の処理
document.addEventListener('keydown', function(event) {
    const key = event.key;
    
    // 数字キー
    if (key >= '0' && key <= '9') {
        appendToExpression(key);
        event.preventDefault();
    }
    // 演算子キー
    else if (['+', '-', '*', '/'].includes(key)) {
        appendToExpression(key);
        event.preventDefault();
    }
    // 括弧キー
    else if (key === '(' || key === ')') {
        appendToExpression(key);
        event.preventDefault();
    }
    // 小数点キー
    else if (key === '.') {
        appendToExpression(key);
        event.preventDefault();
    }
    // Enterキー（計算実行）
    else if (key === 'Enter') {
        const form = document.querySelector('form');
        if (form) {
            form.submit();
        }
        event.preventDefault();
    }
    // Escapeキー（クリア）
    else if (key === 'Escape') {
        clearAll();
        event.preventDefault();
    }
    // Backspaceキー
    else if (key === 'Backspace') {
        backspace();
        event.preventDefault();
    }
});

// 計算結果のアニメーション
function animateResult() {
    const resultElement = document.querySelector('.result');
    if (resultElement) {
        resultElement.style.transform = 'scale(1.1)';
        setTimeout(() => {
            resultElement.style.transform = 'scale(1)';
        }, 200);
    }
}

// ボタンクリック時のアニメーション
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('btn')) {
        event.target.style.transform = 'scale(0.95)';
        setTimeout(() => {
            event.target.style.transform = '';
        }, 100);
    }
});

// 履歴アイテムクリック時の処理
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('history-expression')) {
        const expression = event.target.textContent;
        currentExpression = expression;
        updateDisplay();
    }
});

// エラーメッセージの自動非表示
function hideErrorAfterDelay() {
    const errorElement = document.querySelector('.error');
    if (errorElement) {
        setTimeout(() => {
            errorElement.style.opacity = '0';
            setTimeout(() => {
                errorElement.style.display = 'none';
            }, 300);
        }, 3000);
    }
}

// ページ読み込み時にエラー非表示タイマーを開始
document.addEventListener('DOMContentLoaded', function() {
    hideErrorAfterDelay();
});
