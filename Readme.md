# estimate-api 開発用環境セットアップ手順

## 概要

このリポジトリは、Oracle 23c Free（Docker）＋Spring Boot（Doma, POI）＋React（予定）のモノレポ構成です。 
DB・バックエンドはDocker Composeで一括起動可能です。

## 構成
- Oracle 23c Free（コンテナ、APPユーザー自動作成＆シード投入）
- Spring Boot（Doma, POI, etc.）
- .envでの設定値集中管理
- 今後：Reactフロントエンド追加予定

---

## セットアップ手順

### 1. 事前準備
- Docker / Docker Compose が使えること
- Java 21 or 17（ローカルビルド用。通常はDocker上で動作します）

---

### 2. .envファイルの作成

プロジェクトルートに`.env`ファイルを作成し、下記例を参考に設定値を記入してください。

```env
# Oracle DB設定
ORACLE_PWD=oracle
APP_USER=app
APP_USER_PASSWORD=oracle

# Spring Bootデータソース設定
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle-db:1521/FREEPDB1
SPRING_DATASOURCE_USERNAME=app
SPRING_DATASOURCE_PASSWORD=oracle
```

---

### 3. コンテナ起動
プロジェクトルートで以下を実行してください。
```
docker compose up --build
```

- 初回起動時はOracle DBのセットアップとシードデータ投入に数分かかります。
- APPユーザー＆estimateテーブルが自動作成されます。

---

### 4. DB確認方法（任意）
- DBeaver等のクライアントからapp/oracle（ユーザー名/パスワード）で接続し、Service Name: FREEPDB1Port: 1521Host: localhostでDBの内容を参照できます。

---

### 5. Spring Bootアプリへのアクセス
- API/Excelダウンロードエンドポイントなどのパスはapplication.ymlで設定されています。デフォルトで http://localhost:8080 でアクセスできます。

---

### 6. その他
- 設定値はすべて.envに集約しています。変更した場合は、docker compose down -vで一度コンテナとボリュームを削除し、up --buildで再構築してください。

---

### トラブルシュート
- DBコンテナ起動直後はAPPユーザーで接続できない場合があります。数分待ってから再度アクセスしてください。
- シードSQLのエラーはコンテナのログで確認してください。
- estimateテーブルが見つからない場合、PDB（FREEPDB1）・APPユーザーを必ず確認してください。

### DebianベースのLinuxにAmazon Corretto 21をインストールする

このセクションでは、Debian ベースのオペレーティングシステムを実行しているホストまたはコンテナに Amazon Corretto 21 をインストールおよびアンインストールする方法について説明します。

apt を使用する
Ubuntu などの Debian ベースのシステムで Corretto Apt リポジトリを使用するには、Corretto 公開キーをインポートし、次のコマンドを使用してリポジトリをシステム リストに追加します。

```
wget -O - https://apt.corretto.aws/corretto.key | sudo gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | sudo tee /etc/apt/sources.list.d/corretto.list
```
リポジトリを追加したら、次のコマンドを実行して Corretto 21 をインストールできます。

```
sudo apt-get update; sudo apt-get install -y java-21-amazon-corretto-jdk
```