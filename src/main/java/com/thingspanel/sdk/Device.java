package com.thingspanel.sdk;

import java.util.List;
import java.util.Map;

/**
 * 设备信息类
 */
public class Device {
    private String deviceId;
    private String voucher;
    private String deviceNumber;
    private String deviceType;
    private String protocolType;
    private Map<String, Object> config;
    private List<SubDevice> subDevices;

    public Device() {}

    public Device(String deviceId, String voucher, String deviceNumber, String deviceType, String protocolType) {
        this.deviceId = deviceId;
        this.voucher = voucher;
        this.deviceNumber = deviceNumber;
        this.deviceType = deviceType;
        this.protocolType = protocolType;
    }

    // Getter和Setter方法
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public List<SubDevice> getSubDevices() {
        return subDevices;
    }

    public void setSubDevices(List<SubDevice> subDevices) {
        this.subDevices = subDevices;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", voucher='" + voucher + '\'' +
                ", deviceNumber='" + deviceNumber + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", config=" + config +
                ", subDevices=" + subDevices +
                '}';
    }
} 