# 電卓アプリ (Calculator App)

Java21 + SpringBoot + Thymeleafを使用したWeb電卓アプリケーション

## 概要

このアプリケーションは、Webブラウザ上で動作する電卓機能を提供します。四則演算、括弧を含む複雑な計算、計算履歴の表示などの機能を備えています。

## 技術スタック

- **Java**: 21
- **フレームワーク**: Spring Boot 3.2.0
- **テンプレートエンジン**: Thymeleaf
- **ビルドツール**: Maven
- **テスト**: JUnit 5, MockMvc
- **フロントエンド**: HTML5, CSS3, JavaScript

## 機能

### 基本機能
- 四則演算 (+, -, *, /)
- 括弧を含む複雑な計算
- 小数点計算
- 計算履歴の表示（最大10件）
- 履歴のクリア機能

### エラーハンドリング
- ゼロ除算エラー
- 無効な式の検出
- 括弧の不一致チェック
- 入力値のバリデーション

### UI/UX
- レスポンシブデザイン
- キーボード入力対応
- アニメーション効果
- モダンなデザイン

## セットアップ

### 前提条件
- Java 21以上
- Maven 3.6以上

### インストールと実行

1. リポジトリをクローン
```bash
git clone <repository-url>
cd test-calc-java-subagent-b
```

2. アプリケーションをビルド
```bash
mvn clean compile
```

3. テストを実行
```bash
mvn test
```

4. アプリケーションを起動
```bash
mvn spring-boot:run
```

5. ブラウザでアクセス
```
http://localhost:8080/calculator
```

## 使用方法

### Web UI
1. ブラウザで `http://localhost:8080/calculator` にアクセス
2. ボタンをクリックまたはキーボードで数式を入力
3. `=` ボタンをクリックまたはEnterキーで計算実行
4. 計算履歴は右側に表示されます

### キーボードショートカット
- `Enter`: 計算実行
- `Escape`: クリア
- `Backspace`: 最後の文字を削除
- `0-9`: 数字入力
- `+`, `-`, `*`, `/`: 演算子入力
- `(`, `)`: 括弧入力
- `.`: 小数点入力

### API
計算APIも提供されています：

```bash
curl -X POST http://localhost:8080/calculator/api/calculate \
  -H "Content-Type: application/json" \
  -d '{"expression": "2 + 3 * 4"}'
```

## プロジェクト構造

```
src/
├── main/
│   ├── java/com/example/calculator/
│   │   ├── CalculatorApplication.java          # メインアプリケーション
│   │   ├── controller/
│   │   │   └── CalculatorController.java       # コントローラー
│   │   ├── service/
│   │   │   └── CalculatorService.java          # ビジネスロジック
│   │   └── model/
│   │       ├── CalculationRequest.java         # リクエストモデル
│   │       └── CalculationResult.java          # レスポンスモデル
│   └── resources/
│       ├── application.properties              # 設定ファイル
│       ├── static/
│       │   ├── css/calculator.css              # スタイルシート
│       │   └── js/calculator.js                # JavaScript
│       └── templates/
│           └── calculator.html                 # Thymeleafテンプレート
└── test/
    └── java/com/example/calculator/
        ├── CalculatorApplicationTests.java     # 統合テスト
        ├── controller/
        │   └── CalculatorControllerTest.java   # コントローラーテスト
        └── service/
            └── CalculatorServiceTest.java      # サービステスト
```

## テスト

### テスト実行
```bash
# 全テスト実行
mvn test

# 特定のテストクラス実行
mvn test -Dtest=CalculatorServiceTest

# テストカバレッジレポート生成
mvn jacoco:report
```

### テストカバレッジ
- 単体テスト: CalculatorServiceTest
- 統合テスト: CalculatorControllerTest
- アプリケーションテスト: CalculatorApplicationTests

## 開発

### 開発環境セットアップ
1. IDEでプロジェクトを開く
2. Java 21のSDKを設定
3. Mavenプロジェクトとして認識させる
4. 依存関係をダウンロード

### コード品質
- コードカバレッジ: 80%以上を目標
- 静的解析: SpotBugs, Checkstyle（今後追加予定）
- フォーマット: Google Java Style（今後追加予定）

## デプロイ

### JARファイル作成
```bash
mvn clean package
```

### Docker（今後追加予定）
```bash
docker build -t calculator-app .
docker run -p 8080:8080 calculator-app
```

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

プルリクエストやイシューの報告を歓迎します。

## 更新履歴

- v0.0.1: 初期リリース
  - 基本的な四則演算機能
  - Web UI
  - 計算履歴機能
  - テスト実装