#!/bin/bash

# ThingsPanel Protocol SDK Java 构建脚本

echo "=== ThingsPanel Protocol SDK Java 构建脚本 ==="

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven未安装，请先安装Maven"
    exit 1
fi

# 清理之前的构建
echo "清理之前的构建..."
mvn clean

# 编译项目
echo "编译项目..."
mvn compile

# 运行测试
echo "运行测试..."
mvn test

# 打包项目
echo "打包项目..."
mvn package

# 生成源码包和文档包
echo "生成源码包和文档包..."
mvn source:jar javadoc:jar

echo "构建完成！"
echo "生成的文件:"
echo "  target/tp-protocol-sdk-java-1.0.0.jar - 主jar包"
echo "  target/tp-protocol-sdk-java-1.0.0-sources.jar - 源码包"
echo "  target/tp-protocol-sdk-java-1.0.0-javadoc.jar - 文档包"

# 显示jar包信息
if [ -f "target/tp-protocol-sdk-java-1.0.0.jar" ]; then
    echo ""
    echo "主jar包信息:"
    jar tf target/tp-protocol-sdk-java-1.0.0.jar | head -20
    echo "..."
    echo "总文件数: $(jar tf target/tp-protocol-sdk-java-1.0.0.jar | wc -l)"
fi 