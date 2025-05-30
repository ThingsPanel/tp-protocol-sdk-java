package com.thingspanel.sdk;

/**
 * 子设备信息类
 */
public class SubDevice {
    private String deviceId;
    private String voucher;
    private String deviceNumber;
    private String address;

    public SubDevice() {}

    public SubDevice(String deviceId, String voucher, String deviceNumber, String address) {
        this.deviceId = deviceId;
        this.voucher = voucher;
        this.deviceNumber = deviceNumber;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubDevice{" +
                "deviceId='" + deviceId + '\'' +
                ", voucher='" + voucher + '\'' +
                ", deviceNumber='" + deviceNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
} 