# DebianベースのLinuxにAmazon Corretto 21をインストールする

このセクションでは、Debian ベースのオペレーティングシステムを実行しているホストまたはコンテナに Amazon Corretto 21 をインストールおよびアンインストールする方法について説明します。

apt を使用する
Ubuntu などの Debian ベースのシステムで Corretto Apt リポジトリを使用するには、Corretto 公開キーをインポートし、次のコマンドを使用してリポジトリをシステム リストに追加します。


wget -O - https://apt.corretto.aws/corretto.key | sudo gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | sudo tee /etc/apt/sources.list.d/corretto.list
リポジトリを追加したら、次のコマンドを実行して Corretto 21 をインストールできます。


sudo apt-get update; sudo apt-get install -y java-21-amazon-corretto-jdk