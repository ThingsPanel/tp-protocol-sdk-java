package com.thingspanel.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TPClient单元测试
 */
public class TPClientTest {
    
    private TPClient client;
    
    @BeforeEach
    public void setUp() {
        client = new TPClient("tcp://localhost:1883")
            .setAuth("test_user", "test_pass")
            .setClientId("test-client-001");
    }
    
    @Test
    public void testClientCreation() {
        assertNotNull(client);
        assertEquals("test-client-001", client.getClientId());
        assertFalse(client.isConnected());
    }
    
    @Test
    public void testSetServiceIdentifier() {
        client.setServiceIdentifier("test-service");
        assertEquals("test-service", client.getServiceIdentifier());
    }
    
    @Test
    public void testConnect() {
        boolean connected = client.connect();
        assertTrue(connected);
        assertTrue(client.isConnected());
        
        client.disconnect();
        assertFalse(client.isConnected());
    }
    
    @Test
    public void testFullClientCreation() {
        TPClient fullClient = new TPClient("tcp://localhost:1883", "http://localhost:8080");
        assertNotNull(fullClient);
        
        // 测试API功能需要连接
        fullClient.setServiceIdentifier("test-service");
        if (fullClient.connect()) {
            // 这些调用在模拟环境下会正常执行
            assertDoesNotThrow(() -> {
                fullClient.deviceDynamicAuth("secret", "device001");
                fullClient.getDeviceConfig("device001", "voucher", "DEV001");
                fullClient.sendHeartbeat();
            });
            fullClient.disconnect();
        }
    }
} 