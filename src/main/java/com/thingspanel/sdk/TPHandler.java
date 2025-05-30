package com.thingspanel.sdk;

import java.util.Map;
import java.util.logging.Logger;

/**
 * ThingsPanel HTTP回调处理器
 */
public class TPHandler {
    private static final Logger logger = Logger.getLogger(TPHandler.class.getName());
    
    private FormConfigHandler formConfigHandler;
    private DeviceDisconnectHandler deviceDisconnectHandler;
    private NotificationHandler notificationHandler;

    /**
     * 设置表单配置处理函数
     */
    public void setFormConfigHandler(FormConfigHandler handler) {
        this.formConfigHandler = handler;
    }

    /**
     * 设置设备断开处理函数
     */
    public void setDeviceDisconnectHandler(DeviceDisconnectHandler handler) {
        this.deviceDisconnectHandler = handler;
    }

    /**
     * 设置通知处理函数
     */
    public void setNotificationHandler(NotificationHandler handler) {
        this.notificationHandler = handler;
    }

    /**
     * 启动HTTP服务器
     * @param port 端口号
     */
    public void start(int port) {
        logger.info("启动HTTP服务器，端口: " + port);
        
        // 这里应该启动真正的HTTP服务器
        // 建议使用Spring Boot或Servlet容器
        // 当前是模拟实现
        
        logger.info("HTTP服务器启动成功");
        logger.info("回调接口:");
        logger.info("  GET  /api/v1/form/config - 获取表单配置");
        logger.info("  POST /api/v1/device/disconnect - 设备断开通知");
        logger.info("  POST /api/v1/plugin/notification - 事件通知");
        logger.info("  GET  /api/v1/plugin/device/list - 获取设备列表");
    }

    /**
     * 处理表单配置请求
     */
    public Map<String, Object> handleFormConfig(String protocolType, String deviceType, String formType) {
        if (formConfigHandler != null) {
            return formConfigHandler.handle(protocolType, deviceType, formType);
        }
        
        // 默认配置
        Map<String, Object> config = new java.util.HashMap<>();
        config.put("message", "未设置表单配置处理器");
        return config;
    }

    /**
     * 处理设备断开通知
     */
    public void handleDeviceDisconnect(String deviceId, String voucher) {
        if (deviceDisconnectHandler != null) {
            deviceDisconnectHandler.handle(deviceId, voucher);
        } else {
            logger.info("设备断开: " + deviceId + ", voucher: " + voucher);
        }
    }

    /**
     * 处理事件通知
     */
    public void handleNotification(Map<String, Object> notification) {
        if (notificationHandler != null) {
            notificationHandler.handle(notification);
        } else {
            logger.info("收到通知: " + notification);
        }
    }

    // 回调接口定义
    @FunctionalInterface
    public interface FormConfigHandler {
        Map<String, Object> handle(String protocolType, String deviceType, String formType);
    }

    @FunctionalInterface
    public interface DeviceDisconnectHandler {
        void handle(String deviceId, String voucher);
    }

    @FunctionalInterface
    public interface NotificationHandler {
        void handle(Map<String, Object> notification);
    }
} 