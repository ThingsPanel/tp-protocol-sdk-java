package com.thingspanel.sdk;

import java.util.*;

/**
 * ThingsPanel Protocol SDK Java使用示例
 */
public class Example {
    public static void main(String[] args) {
        System.out.println("=== ThingsPanel Protocol SDK Java 示例 ===");
        
        // 示例1: 完整客户端使用
        fullClientExample();
        
        // 示例2: MQTT客户端使用
        mqttClientExample();
        
        // 示例3: HTTP回调处理器使用
        httpHandlerExample();
        
        // 示例4: 设备管理
        deviceManagementExample();
    }

    /**
     * 完整客户端使用示例
     */
    public static void fullClientExample() {
        System.out.println("\n--- 完整客户端示例 ---");
        
        // 创建完整客户端（包含MQTT和API功能）
        TPClient client = new TPClient("tcp://localhost:1883", "http://localhost:8080")
            .setAuth("username", "password")
            .setClientId("my-plugin-001")
            .setServiceIdentifier("my-service");

        // 连接（会自动发送心跳）
        if (client.connect()) {
            System.out.println("客户端连接成功");
            
            // 1. 设备动态认证
            TPAPIClient.APIResponse<Map<String, String>> authResponse = 
                client.deviceDynamicAuth("template-secret-123", "device-001");
            System.out.println("设备动态认证: " + authResponse);
            
            // 2. 获取设备配置
            TPAPIClient.APIResponse<Device> configResponse = 
                client.getDeviceConfig("device-001", "voucher-123", "DEV001");
            System.out.println("设备配置: " + configResponse);
            
            // 3. 发送设备数据
            String data = "{\"temperature\":25.5,\"humidity\":60}";
            client.sendData("my-service", "device-001", data);
            
            // 4. 订阅下行消息
            client.subscribe("plugin/my-service/devices/+/commands");
            
            // 5. 发送心跳
            TPAPIClient.APIResponse<String> heartbeatResponse = client.sendHeartbeat();
            System.out.println("心跳响应: " + heartbeatResponse);
            
            client.disconnect();
        }
    }

    /**
     * MQTT客户端使用示例
     */
    public static void mqttClientExample() {
        System.out.println("\n--- MQTT客户端示例 ---");
        
        // 创建客户端
        TPClient client = new TPClient("tcp://localhost:1883")
            .setAuth("username", "password")
            .setClientId("my-device-001");

        // 连接到服务器
        if (client.connect()) {
            System.out.println("MQTT连接成功");
            
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
    }

    /**
     * HTTP回调处理器使用示例
     */
    public static void httpHandlerExample() {
        System.out.println("\n--- HTTP回调处理器示例 ---");
        
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
            System.out.println("处理表单配置请求: " + protocolType + "/" + deviceType + "/" + formType);
            return config;
        });

        // 设置设备断开处理函数
        handler.setDeviceDisconnectHandler((deviceId, voucher) -> {
            System.out.println("设备断开: " + deviceId + ", voucher: " + voucher);
        });

        // 设置通知处理函数
        handler.setNotificationHandler((notification) -> {
            System.out.println("收到通知: " + notification);
        });

        // 启动HTTP服务器
        handler.start(8080);
        
        // 模拟处理请求
        Map<String, Object> formConfig = handler.handleFormConfig("mqtt", "sensor", "device");
        System.out.println("表单配置: " + formConfig);
        
        handler.handleDeviceDisconnect("device-001", "voucher-123");
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "device_online");
        notification.put("device_id", "device-001");
        handler.handleNotification(notification);
    }

    /**
     * 设备管理示例
     */
    public static void deviceManagementExample() {
        System.out.println("\n--- 设备管理示例 ---");
        
        // 创建设备
        Device device = new Device("device-001", "voucher-123", "DEV001", "sensor", "mqtt");

        // 设置设备配置
        Map<String, Object> config = new HashMap<>();
        config.put("host", "192.168.1.100");
        config.put("port", 1883);
        config.put("username", "device_user");
        config.put("password", "device_pass");
        device.setConfig(config);

        // 创建子设备
        SubDevice subDevice1 = new SubDevice("sub-001", "sub-voucher-1", "SUB001", "addr-001");
        SubDevice subDevice2 = new SubDevice("sub-002", "sub-voucher-2", "SUB002", "addr-002");
        List<SubDevice> subDevices = new ArrayList<>();
        subDevices.add(subDevice1);
        subDevices.add(subDevice2);
        device.setSubDevices(subDevices);

        System.out.println("设备信息: " + device);
        System.out.println("子设备数量: " + device.getSubDevices().size());
        
        for (SubDevice subDevice : device.getSubDevices()) {
            System.out.println("  子设备: " + subDevice);
        }
    }
} 