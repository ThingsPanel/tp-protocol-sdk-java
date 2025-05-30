@echo off
REM ThingsPanel Protocol SDK Java 构建脚本 (Windows)

echo === ThingsPanel Protocol SDK Java 构建脚本 ===

REM 检查Maven是否安装
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: Maven未安装，请先安装Maven
    pause
    exit /b 1
)

REM 清理之前的构建
echo 清理之前的构建...
call mvn clean

REM 编译项目
echo 编译项目...
call mvn compile

REM 运行测试
echo 运行测试...
call mvn test

REM 打包项目
echo 打包项目...
call mvn package

REM 生成源码包和文档包
echo 生成源码包和文档包...
call mvn source:jar javadoc:jar

echo 构建完成！
echo 生成的文件:
echo   target\tp-protocol-sdk-java-1.0.0.jar - 主jar包
echo   target\tp-protocol-sdk-java-1.0.0-sources.jar - 源码包
echo   target\tp-protocol-sdk-java-1.0.0-javadoc.jar - 文档包

REM 显示jar包信息
if exist "target\tp-protocol-sdk-java-1.0.0.jar" (
    echo.
    echo 主jar包信息:
    jar tf target\tp-protocol-sdk-java-1.0.0.jar
)

pause 