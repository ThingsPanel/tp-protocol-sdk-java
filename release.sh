#!/bin/bash

# ThingsPanel Protocol SDK Java 发布脚本

echo "=== ThingsPanel Protocol SDK Java 发布脚本 ==="

# 检查必要的环境变量
if [ -z "$OSSRH_USERNAME" ] || [ -z "$OSSRH_PASSWORD" ]; then
    echo "错误: 请设置OSSRH_USERNAME和OSSRH_PASSWORD环境变量"
    echo "export OSSRH_USERNAME=your_username"
    echo "export OSSRH_PASSWORD=your_password"
    exit 1
fi

# 检查GPG密钥
if ! gpg --list-secret-keys | grep -q "sec"; then
    echo "错误: 未找到GPG密钥，请先生成GPG密钥"
    echo "gpg --gen-key"
    exit 1
fi

echo "准备发布到Maven Central..."

# 设置Maven settings.xml中的服务器配置
echo "配置Maven服务器认证..."
mkdir -p ~/.m2
cat > ~/.m2/settings.xml << EOF
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>\${env.OSSRH_USERNAME}</username>
      <password>\${env.OSSRH_PASSWORD}</password>
    </server>
  </servers>
</settings>
EOF

# 清理并构建
echo "清理并构建项目..."
mvn clean

# 发布到Maven Central
echo "发布到Maven Central..."
mvn deploy -P release

echo "发布完成！"
echo "请到 https://s01.oss.sonatype.org/ 查看发布状态" 