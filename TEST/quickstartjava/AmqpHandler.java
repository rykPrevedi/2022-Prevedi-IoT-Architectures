package it.unimore.tirocinio.prevedi.iot.architectures.quickstartjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Prevedi
 * @created 16/01/2023 - 17:14
 * @project iot-architectures
 */

public class AmqpHandler {
private static final Logger logger = LoggerFactory.getLogger(AmqpHandler.class);
private static final String deviceRegistryAddress = "http://192.168.181.13";
private static final int deviceRegistryPort = 1080;
private static final String devicePassword = "my-secret-password";

    public static void main(String[] args) {

        // Configuring Device Registry address and port
        String baseUrl  = String.format("%s:%d", deviceRegistryAddress, deviceRegistryPort);

        DeviceRegistryManagement deviceRegistry = new DeviceRegistryManagement(baseUrl, devicePassword);

        // Register Tenant
        deviceRegistry.createTenant("/v1/tenants");
        System.out.println("Registered tenant: " + deviceRegistry.getTenantId());

        // Add Device to Tenant
        deviceRegistry.addDevice("/v1/devices/" + deviceRegistry.getTenantId());
        System.out.println("Registered device: " + deviceRegistry.getDeviceId());

        // Set Device Password
        deviceRegistry.setDevicePassword("/v1/credentials/" + deviceRegistry.getTenantId() + "/" + deviceRegistry.getDeviceId());

    }
}
