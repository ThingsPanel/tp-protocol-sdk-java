# ThingsPanel Protocol SDK Java 使用指南

## 1. 构建和打包

### 1.1 环境要求

- Java 11 或更高版本
- Maven 3.6 或更高版本
- Git（用于版本控制）

### 1.2 构建项目

#### 使用Maven命令

```bash
# 清理项目
mvn clean

# 编译项目
mvn compile

# 运行测试
mvn test

# 打包项目
mvn package

# 生成源码包和文档包
mvn source:jar javadoc:jar
```

#### 使用构建脚本

**Linux/macOS:**
```bash
chmod +x build.sh
./build.sh
```

**Windows:**
```cmd
build.bat
```

### 1.3 构建输出

构建完成后，在 `target/` 目录下会生成以下文件：

- `tp-protocol-sdk-java-1.0.0.jar` - 主jar包
- `tp-protocol-sdk-java-1.0.0-sources.jar` - 源码包
- `tp-protocol-sdk-java-1.0.0-javadoc.jar` - 文档包

## 2. 本地使用

### 2.1 直接使用jar包

```bash
# 将jar包添加到classpath
java -cp "tp-protocol-sdk-java-1.0.0.jar:your-app.jar" com.yourpackage.YourApp
```

### 2.2 Maven项目中使用

在你的项目的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.thingspanel</groupId>
    <artifactId>tp-protocol-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2.3 Gradle项目中使用

在你的项目的 `build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation 'com.thingspanel:tp-protocol-sdk-java:1.0.0'
}
```

## 3. 发布到开源仓库

### 3.1 发布到Maven Central

#### 准备工作

1. **注册Sonatype账号**
   - 访问 https://issues.sonatype.org/
   - 创建账号并申请groupId

2. **生成GPG密钥**
   ```bash
   # 生成密钥
   gpg --gen-key
   
   # 查看密钥
   gpg --list-keys
   
   # 上传公钥到服务器
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   ```

3. **配置环境变量**
   ```bash
   export OSSRH_USERNAME=your_username
   export OSSRH_PASSWORD=your_password
   ```

#### 发布命令

```bash
# 使用发布脚本
chmod +x release.sh
./release.sh

# 或者直接使用Maven命令
mvn clean deploy -P release
```

### 3.2 发布到GitHub Packages

1. **创建GitHub Personal Access Token**
   - 访问 GitHub Settings > Developer settings > Personal access tokens
   - 创建token，勾选 `write:packages` 权限

2. **配置Maven settings.xml**
   ```xml
   <settings>
     <servers>
       <server>
         <id>github</id>
         <username>YOUR_GITHUB_USERNAME</username>
         <password>YOUR_GITHUB_TOKEN</password>
       </server>
     </servers>
   </settings>
   ```

3. **修改pom.xml添加GitHub仓库**
   ```xml
   <distributionManagement>
     <repository>
       <id>github</id>
       <name>GitHub ThingsPanel Apache Maven Packages</name>
       <url>https://maven.pkg.github.com/ThingsPanel/tp-protocol-sdk-java</url>
     </repository>
   </distributionManagement>
   ```

4. **发布**
   ```bash
   mvn deploy
   ```

### 3.3 发布到JitPack

JitPack可以直接从GitHub仓库构建和发布包：

1. **推送代码到GitHub**
   ```bash
   git add .
   git commit -m "Release v1.0.0"
   git tag v1.0.0
   git push origin main --tags
   ```

2. **访问JitPack**
   - 访问 https://jitpack.io/
   - 输入GitHub仓库地址：`ThingsPanel/tp-protocol-sdk-java`
   - 点击"Get it"构建包

3. **使用JitPack包**
   ```xml
   <repositories>
     <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
     </repository>
   </repositories>
   
   <dependency>
     <groupId>com.github.ThingsPanel</groupId>
     <artifactId>tp-protocol-sdk-java</artifactId>
     <version>v1.0.0</version>
   </dependency>
   ```

## 4. 使用示例

### 4.1 基本使用

```java
import com.thingspanel.sdk.*;

// 创建客户端
TPClient client = new TPClient("tcp://localhost:1883", "http://localhost:8080")
    .setAuth("username", "password")
    .setClientId("my-plugin-001")
    .setServiceIdentifier("my-service");

// 连接并使用
if (client.connect()) {
    // 发送数据
    client.sendData("my-service", "device-001", "{\"temperature\":25.5}");
    
    // 获取设备配置
    var response = client.getDeviceConfig("device-001", "voucher", "DEV001");
    
    client.disconnect();
}
```

### 4.2 HTTP回调处理

```java
import com.thingspanel.sdk.*;

TPHandler handler = new TPHandler();

// 设置处理函数
handler.setFormConfigHandler((protocolType, deviceType, formType) -> {
    // 返回表单配置
    Map<String, Object> config = new HashMap<>();
    // ... 配置逻辑
    return config;
});

// 启动服务器
handler.start(8080);
```

## 5. 版本管理

### 5.1 版本号规则

采用语义化版本控制（Semantic Versioning）：

- `MAJOR.MINOR.PATCH`
- 例如：`1.0.0`, `1.1.0`, `1.1.1`

### 5.2 发布新版本

1. **更新版本号**
   ```xml
   <!-- pom.xml -->
   <version>1.1.0</version>
   ```

2. **创建Git标签**
   ```bash
   git tag v1.1.0
   git push origin v1.1.0
   ```

3. **发布**
   ```bash
   ./release.sh
   ```

## 6. 故障排除

### 6.1 常见问题

**问题1: Maven编译失败**
```
解决方案: 检查Java版本是否为11+，Maven版本是否为3.6+
```

**问题2: GPG签名失败**
```
解决方案: 
1. 检查GPG密钥是否存在：gpg --list-secret-keys
2. 配置GPG密钥：mvn clean deploy -Dgpg.keyname=YOUR_KEY_ID
```

**问题3: 发布到Maven Central失败**
```
解决方案:
1. 检查OSSRH用户名密码是否正确
2. 检查groupId是否已申请
3. 检查GPG签名是否正确
```

### 6.2 调试技巧

```bash
# 查看详细构建日志
mvn clean package -X

# 跳过测试
mvn clean package -DskipTests

# 只运行特定测试
mvn test -Dtest=TPClientTest
```

## 7. 贡献指南

### 7.1 开发环境设置

```bash
# 克隆项目
git clone https://github.com/ThingsPanel/tp-protocol-sdk-java.git
cd tp-protocol-sdk-java

# 构建项目
./build.sh

# 运行测试
mvn test
```

### 7.2 提交代码

```bash
# 创建功能分支
git checkout -b feature/new-feature

# 提交更改
git add .
git commit -m "Add new feature"

# 推送分支
git push origin feature/new-feature

# 创建Pull Request
```

## 8. 许可证

本项目采用 GNU Affero General Public License v3.0 许可证。详见 [LICENSE](LICENSE) 文件。 