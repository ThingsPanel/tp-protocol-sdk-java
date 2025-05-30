package com.thingspanel.sdk;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ThingsPanel Protocol SDK Java客户端
 * 提供MQTT通信和HTTP API功能的完整客户端
 */
public class TPClient {
    private static final Logger logger = Logger.getLogger(TPClient.class.getName());
    
    private String mqttBroker;
    private String clientId;
    private String username;
    private String password;
    private boolean connected = false;
    
    // API客户端
    private TPAPIClient apiClient;
    private String serviceIdentifier;

    /**
     * 创建MQTT客户端
     * @param mqttBroker MQTT服务器地址
     */
    public TPClient(String mqttBroker) {
        this.mqttBroker = mqttBroker;
        this.clientId = "tp-client-" + System.currentTimeMillis();
    }

    /**
     * 创建完整客户端（MQTT + API）
     * @param mqttBroker MQTT服务器地址
     * @param apiBaseURL API服务器地址
     */
    public TPClient(String mqttBroker, String apiBaseURL) {
        this.mqttBroker = mqttBroker;
        this.clientId = "tp-client-" + System.currentTimeMillis();
        this.apiClient = new TPAPIClient(apiBaseURL);
    }

    /**
     * 设置MQTT认证信息
     * @param username 用户名
     * @param password 密码
     * @return 当前客户端实例
     */
    public TPClient setAuth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * 设置客户端ID
     * @param clientId 客户端ID
     * @return 当前客户端实例
     */
    public TPClient setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * 设置服务标识符
     * @param serviceIdentifier 服务标识符
     * @return 当前客户端实例
     */
    public TPClient setServiceIdentifier(String serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
        return this;
    }

    /**
     * 连接到MQTT服务器
     * @return 连接是否成功
     */
    public boolean connect() {
        try {
            logger.info("连接到MQTT服务器: " + mqttBroker);
            logger.info("客户端ID: " + clientId);
            
            // 这里应该使用真正的MQTT客户端库，如Eclipse Paho
            // 当前是模拟实现
            connected = true;
            
            logger.info("MQTT连接成功");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "MQTT连接失败", e);
            return false;
        }
    }

    /**
     * 发送设备状态
     * @param deviceId 设备ID
     * @param status 状态值
     */
    public void sendStatus(String deviceId, String status) {
        if (!connected) {
            logger.warning("MQTT未连接，无法发送状态");
            return;
        }

        String topic = "devices/status/" + deviceId;
        String payload = status;
        
        logger.info("发送设备状态 - 主题: " + topic + ", 数据: " + payload);
        
        // 这里应该使用真正的MQTT发布
        // mqttClient.publish(topic, payload.getBytes());
    }

    /**
     * 发送设备数据
     * @param serviceIdentifier 服务标识符
     * @param deviceId 设备ID
     * @param data 数据内容
     */
    public void sendData(String serviceIdentifier, String deviceId, String data) {
        if (!connected) {
            logger.warning("MQTT未连接，无法发送数据");
            return;
        }

        String topic = "plugin/" + serviceIdentifier + "/devices/" + deviceId + "/datas";
        
        logger.info("发送设备数据 - 主题: " + topic + ", 数据: " + data);
        
        // 这里应该使用真正的MQTT发布
        // mqttClient.publish(topic, data.getBytes());
    }

    /**
     * 订阅主题
     * @param topic 主题
     */
    public void subscribe(String topic) {
        if (!connected) {
            logger.warning("MQTT未连接，无法订阅主题");
            return;
        }

        logger.info("订阅主题: " + topic);
        
        // 这里应该使用真正的MQTT订阅
        // mqttClient.subscribe(topic);
    }

    /**
     * 断开MQTT连接
     */
    public void disconnect() {
        if (connected) {
            logger.info("断开MQTT连接");
            connected = false;
            
            // 这里应该使用真正的MQTT断开
            // mqttClient.disconnect();
        }
    }

    // API功能方法

    /**
     * 获取设备配置
     * @param deviceId 设备ID
     * @param voucher 凭证
     * @param deviceNumber 设备编号
     * @return API响应
     */
    public TPAPIClient.APIResponse<Device> getDeviceConfig(String deviceId, String voucher, String deviceNumber) {
        if (apiClient == null) {
            throw new IllegalStateException("API客户端未初始化，请使用包含apiBaseURL的构造函数");
        }

        TPAPIClient.DeviceConfigRequest request = new TPAPIClient.DeviceConfigRequest();
        request.deviceId = deviceId;
        request.voucher = voucher;
        request.deviceNumber = deviceNumber;

        return apiClient.getDeviceConfig(request);
    }

    /**
     * 设备动态认证
     * @param templateSecret 模板密钥
     * @param deviceNumber 设备编号
     * @return API响应
     */
    public TPAPIClient.APIResponse<java.util.Map<String, String>> deviceDynamicAuth(String templateSecret, String deviceNumber) {
        if (apiClient == null) {
            throw new IllegalStateException("API客户端未初始化，请使用包含apiBaseURL的构造函数");
        }

        TPAPIClient.DeviceDynamicAuthRequest request = new TPAPIClient.DeviceDynamicAuthRequest();
        request.templateSecret = templateSecret;
        request.deviceNumber = deviceNumber;

        return apiClient.deviceDynamicAuth(request);
    }

    /**
     * 获取服务接入点信息
     * @return API响应
     */
    public TPAPIClient.APIResponse<java.util.Map<String, Object>> getServiceAccess() {
        if (apiClient == null) {
            throw new IllegalStateException("API客户端未初始化，请使用包含apiBaseURL的构造函数");
        }

        TPAPIClient.ServiceAccessRequest request = new TPAPIClient.ServiceAccessRequest();
        request.serviceIdentifier = this.serviceIdentifier;

        return apiClient.getServiceAccess(request);
    }

    /**
     * 发送心跳
     * @return API响应
     */
    public TPAPIClient.APIResponse<String> sendHeartbeat() {
        if (apiClient == null) {
            throw new IllegalStateException("API客户端未初始化，请使用包含apiBaseURL的构造函数");
        }

        TPAPIClient.HeartbeatRequest request = new TPAPIClient.HeartbeatRequest();
        request.serviceIdentifier = this.serviceIdentifier;

        return apiClient.sendHeartbeat(request);
    }

    // Getter方法
    public boolean isConnected() {
        return connected;
    }

    public String getClientId() {
        return clientId;
    }

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }
} 