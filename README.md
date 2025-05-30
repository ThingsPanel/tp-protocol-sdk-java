# TP Protocol SDK Java

ThingsPanel Protocol SDK for Java，用于快速开发ThingsPanel插件的Java版本。

## 功能特性

- 设备配置管理
- MQTT消息通信
- HTTP回调处理
- HTTP API客户端
- 设备动态认证
- 服务接入点管理
- 心跳机制
- 简单易用的API

## 快速开始

### 完整客户端使用（推荐）

```java
// 创建完整客户端（包含MQTT和API功能）
TPClient client = new TPClient("tcp://localhost:1883", "http://localhost:8080")
    .setAuth("username", "password")
    .setClientId("my-plugin-001")
    .setServiceIdentifier("my-service");

// 连接（会自动发送心跳）
if (client.connect()) {
    // 1. 设备动态认证
    TPAPIClient.APIResponse<Map<String, String>> authResponse = 
        client.deviceDynamicAuth("template-secret-123", "device-001");
    
    // 2. 获取设备配置
    TPAPIClient.APIResponse<Device> configResponse = 
        client.getDeviceConfig("device-001", "voucher-123", "DEV001");
    
    // 3. 发送设备数据
    String data = "{\"temperature\":25.5,\"humidity\":60}";
    client.sendData("my-service", "device-001", data);
    
    // 4. 订阅下行消息
    client.subscribe("plugin/my-service/devices/+/commands");
    
    // 5. 发送心跳
    client.sendHeartbeat();
    
    client.disconnect();
}
```

### MQTT客户端使用

```java
// 创建客户端
TPClient client = new TPClient("tcp://localhost:1883")
    .setAuth("username", "password")
    .setClientId("my-device-001");

// 连接到服务器
if (client.connect()) {
    // 发送设备状态
    client.sendStatus("device-001", "1");
    
    // 发送设备数据
    String data = "{\"temperature\":25.5,\"humidity\":60}";
    client.sendData("my-service", "device-001", data);
    
    // 订阅主题
    client.subscribe("plugin/my-service/#");
    
    // 断开连接
    client.disconnect();
}
```

### HTTP回调处理器使用

```java
// 创建处理器
TPHandler handler = new TPHandler();

// 设置表单配置处理函数
handler.setFormConfigHandler((protocolType, deviceType, formType) -> {
    Map<String, Object> config = new HashMap<>();
    List<Map<String, Object>> fields = new ArrayList<>();
    
    // 添加配置字段
    Map<String, Object> field = new HashMap<>();
    field.put("name", "host");
    field.put("type", "string");
    field.put("label", "服务器地址");
    fields.add(field);
    
    config.put("fields", fields);
    return config;
});

// 设置设备断开处理函数
handler.setDeviceDisconnectHandler((deviceId, voucher) -> {
    System.out.println("设备断开: " + deviceId);
});

// 启动HTTP服务器
handler.start(8080);
```

### 设备管理

```java
// 创建设备
Device device = new Device("device-001", "voucher-123", "DEV001", "sensor", "mqtt");

// 设置设备配置
Map<String, Object> config = new HashMap<>();
config.put("host", "192.168.1.100");
config.put("port", 1883);
device.setConfig(config);

// 创建子设备
SubDevice subDevice = new SubDevice("sub-001", "sub-voucher", "SUB001", "addr-001");
List<SubDevice> subDevices = new ArrayList<>();
subDevices.add(subDevice);
device.setSubDevices(subDevices);
```

## API说明

### TPClient (主客户端)

#### 构造函数
- `TPClient(String mqttBroker)` - 创建MQTT客户端
- `TPClient(String mqttBroker, String apiBaseURL)` - 创建完整客户端（MQTT + API）

#### 配置方法
- `setAuth(String username, String password)` - 设置MQTT认证信息
- `setClientId(String clientId)` - 设置客户端ID
- `setServiceIdentifier(String serviceIdentifier)` - 设置服务标识符

#### MQTT功能
- `connect()` - 连接到MQTT服务器
- `sendStatus(String deviceId, String status)` - 发送设备状态
- `sendData(String serviceIdentifier, String deviceId, String data)` - 发送设备数据
- `subscribe(String topic)` - 订阅主题
- `disconnect()` - 断开连接

#### API功能
- `getDeviceConfig(String deviceId, String voucher, String deviceNumber)` - 获取设备配置
- `deviceDynamicAuth(String templateSecret, String deviceNumber)` - 设备动态认证
- `getServiceAccess()` - 获取服务接入点信息
- `sendHeartbeat()` - 发送心跳

### TPAPIClient (API客户端)

- `getDeviceConfig(DeviceConfigRequest)` - 获取设备配置
- `deviceDynamicAuth(DeviceDynamicAuthRequest)` - 设备动态认证
- `getServiceAccess(ServiceAccessRequest)` - 获取服务接入点
- `sendHeartbeat(HeartbeatRequest)` - 发送心跳

### TPHandler (HTTP回调处理器)

- `setFormConfigHandler(FormConfigHandler handler)` - 设置表单配置处理函数
- `setDeviceDisconnectHandler(DeviceDisconnectHandler handler)` - 设置设备断开处理函数
- `setNotificationHandler(NotificationHandler handler)` - 设置通知处理函数
- `start(int port)` - 启动HTTP服务器

### HTTP回调接口

- `/api/v1/form/config` - 获取表单配置
- `/api/v1/device/disconnect` - 设备断开通知
- `/api/v1/plugin/notification` - 事件通知
- `/api/v1/plugin/device/list` - 获取设备列表

### HTTP API接口

- `/api/v1/plugin/device/config` - 获取设备配置
- `/api/v1/device/auth` - 设备动态认证
- `/api/v1/plugin/service/access` - 获取服务接入点
- `/api/v1/plugin/heartbeat` - 发送心跳

### MQTT主题

- `devices/status/{device_id}` - 设备状态上报
- `plugin/{服务标识符}/devices/{device_id}/datas` - 设备数据上报
- `plugin/{服务标识符}/#` - 订阅平台数据主题

## 文件结构

```
tp-protocol-sdk-java/
├── Device.java          - 设备信息类
├── SubDevice.java       - 子设备信息类
├── TPClient.java        - 主客户端（MQTT + API）
├── TPAPIClient.java     - HTTP API客户端
├── TPHandler.java       - HTTP回调处理器
├── Example.java         - 使用示例
└── README.md           - 说明文档
```

## 运行示例

```bash
# 编译所有Java文件
javac *.java

# 运行完整示例
java Example
```

## 功能对比

### 与Go版本功能对比

| 功能 | Go版本 | Java版本 | 状态 |
|------|--------|----------|------|
| MQTT客户端 | ✅ | ✅ | 完整实现 |
| HTTP回调处理 | ✅ | ✅ | 完整实现 |
| 设备配置获取 | ✅ | ✅ | 完整实现 |
| 设备动态认证 | ✅ | ✅ | 完整实现 |
| 服务接入点管理 | ✅ | ✅ | 完整实现 |
| 心跳机制 | ✅ | ✅ | 完整实现 |
| 数据类型定义 | ✅ | ✅ | 完整实现 |

### 实现差异

1. **Go版本优势**：
   - 使用真实的MQTT库（Eclipse Paho）
   - 更完善的错误处理
   - 更好的并发支持

2. **Java版本特点**：
   - 使用Java 11+ HttpClient
   - 面向对象设计
   - 简化的API接口
   - 更好的类型安全

## 注意事项

1. 这是一个功能完整的SDK框架，包含了Go版本的所有主要功能
2. MQTT功能当前是模拟实现，生产环境需要集成真正的MQTT库（如Eclipse Paho）
3. HTTP服务器功能需要集成Servlet容器或Spring Boot
4. JSON解析当前是简化实现，建议使用Jackson或Gson
5. 生产环境使用时需要添加完善的错误处理、重连机制等

## 依赖建议

实际使用时建议添加以下依赖：

- **MQTT客户端**: Eclipse Paho MQTT Client
- **HTTP服务器**: Spring Boot Starter Web
- **JSON处理**: Jackson或Gson
- **日志框架**: SLF4J + Logback

## License

This project is licensed under the GNU Affero General Public License v3.0 