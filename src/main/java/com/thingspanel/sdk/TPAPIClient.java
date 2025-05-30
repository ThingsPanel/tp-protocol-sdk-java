package com.thingspanel.sdk;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ThingsPanel HTTP API客户端
 */
public class TPAPIClient {
    private static final Logger logger = Logger.getLogger(TPAPIClient.class.getName());
    
    private String baseURL;
    private HttpClient httpClient;

    public TPAPIClient(String baseURL) {
        this.baseURL = baseURL.endsWith("/") ? baseURL.substring(0, baseURL.length() - 1) : baseURL;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * 获取设备配置
     */
    public APIResponse<Device> getDeviceConfig(DeviceConfigRequest request) {
        try {
            String url = baseURL + "/api/v1/plugin/device/config";
            String jsonBody = String.format(
                "{\"device_id\":\"%s\",\"voucher\":\"%s\",\"device_number\":\"%s\"}",
                request.deviceId, request.voucher, request.deviceNumber
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            logger.info("获取设备配置响应: " + response.body());
            
            // 简化的JSON解析，实际应使用Jackson或Gson
            Device device = parseDeviceFromJson(response.body());
            
            return new APIResponse<>(response.statusCode() == 200, response.body(), device);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "获取设备配置失败", e);
            return new APIResponse<>(false, e.getMessage(), null);
        }
    }

    /**
     * 设备动态认证
     */
    public APIResponse<Map<String, String>> deviceDynamicAuth(DeviceDynamicAuthRequest request) {
        try {
            String url = baseURL + "/api/v1/device/auth";
            String jsonBody = String.format(
                "{\"template_secret\":\"%s\",\"device_number\":\"%s\"}",
                request.templateSecret, request.deviceNumber
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            logger.info("设备动态认证响应: " + response.body());
            
            // 简化的JSON解析
            Map<String, String> result = parseMapFromJson(response.body());
            
            return new APIResponse<>(response.statusCode() == 200, response.body(), result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "设备动态认证失败", e);
            return new APIResponse<>(false, e.getMessage(), null);
        }
    }

    /**
     * 获取服务接入点
     */
    public APIResponse<Map<String, Object>> getServiceAccess(ServiceAccessRequest request) {
        try {
            String url = baseURL + "/api/v1/plugin/service/access";
            String jsonBody = String.format(
                "{\"service_identifier\":\"%s\"}",
                request.serviceIdentifier
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            logger.info("获取服务接入点响应: " + response.body());
            
            // 简化的JSON解析
            Map<String, Object> result = parseObjectMapFromJson(response.body());
            
            return new APIResponse<>(response.statusCode() == 200, response.body(), result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "获取服务接入点失败", e);
            return new APIResponse<>(false, e.getMessage(), null);
        }
    }

    /**
     * 发送心跳
     */
    public APIResponse<String> sendHeartbeat(HeartbeatRequest request) {
        try {
            String url = baseURL + "/api/v1/plugin/heartbeat";
            String jsonBody = String.format(
                "{\"service_identifier\":\"%s\"}",
                request.serviceIdentifier
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            logger.info("发送心跳响应: " + response.body());
            
            return new APIResponse<>(response.statusCode() == 200, response.body(), "success");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "发送心跳失败", e);
            return new APIResponse<>(false, e.getMessage(), null);
        }
    }

    // 简化的JSON解析方法（实际应使用专业的JSON库）
    private Device parseDeviceFromJson(String json) {
        Device device = new Device();
        // 这里应该使用真正的JSON解析库
        // 当前是模拟实现
        device.setDeviceId("parsed-device-id");
        device.setVoucher("parsed-voucher");
        return device;
    }

    private Map<String, String> parseMapFromJson(String json) {
        // 简化实现，实际应使用JSON库
        return new java.util.HashMap<>();
    }

    private Map<String, Object> parseObjectMapFromJson(String json) {
        // 简化实现，实际应使用JSON库
        return new java.util.HashMap<>();
    }

    // 请求类
    public static class DeviceConfigRequest {
        public String deviceId;
        public String voucher;
        public String deviceNumber;
    }

    public static class DeviceDynamicAuthRequest {
        public String templateSecret;
        public String deviceNumber;
    }

    public static class ServiceAccessRequest {
        public String serviceIdentifier;
    }

    public static class HeartbeatRequest {
        public String serviceIdentifier;
    }

    // 响应类
    public static class APIResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public APIResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return "APIResponse{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
} 