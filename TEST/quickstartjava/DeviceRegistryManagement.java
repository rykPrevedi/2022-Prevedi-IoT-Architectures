package it.unimore.tirocinio.prevedi.iot.architectures.quickstartjava;

import kong.unirest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class allows to:
 *   Set up a tenant,
 *   Set up a Device in the tenant,
 *   Add credentials to the device
 *
 * @author Riccardo Prevedi
 * @created 16/01/2023 - 17:29
 * @project iot-architectures
 */

public class DeviceRegistryManagement {

    private static final Logger logger = LoggerFactory.getLogger(DeviceRegistryManagement.class);
    private String registryUrl;
    private String tenantId = null;
    private String deviceId = null;
    private String devicePassword;

    public DeviceRegistryManagement(String registryBaseUrl, String devicePassword) {
        this.registryUrl = registryBaseUrl;
        this.devicePassword = devicePassword;
        // Configuring a default base URL to be used for all requests that do not contain a full URL
        Unirest.config().defaultBaseUrl(this.registryUrl);
    }


    /**
     * Create a new tenant with an auto-generated-ID
     *
     * @param resourcePath route parameter "/v1/tenants"
     */
    public void createTenant(String resourcePath) {

        // Making the POST Request. Requests are made when as[Type]() is invoked,
        // No error are made if response was a 200-series and body processing was successful.
        Unirest.post(resourcePath)
                .header("content-type", "application/json")
                .body("{\"ext\": {\"messaging-type\": \"amqp\"}}")
                .asJson()
                .ifSuccess(jsonNodeHttpResponse -> setTenantId(jsonNodeHttpResponse.getBody().getObject().get("id").toString()))
                .ifFailure(jsonNodeHttpResponse -> {
                    logger.error("Oh No! Status {}", jsonNodeHttpResponse.getStatus());
                    jsonNodeHttpResponse.getParsingError().ifPresent(exception -> {
                        logger.error("Parsing Exception: ", exception);
                        logger.error("Original body: {}", exception.getOriginalBody());
                    });
                });
    }

    /**
     * Create a new device registration with auto-generated ID
     *
     * @param resourcePath route parameter "/v1/devices/{tenantId}"
     */
    public void addDevice(String resourcePath) {

        Unirest.post(resourcePath)
                .header("content-type", "application/json")
                .asJson()
                .ifSuccess(jsonNodeHttpResponse -> setDeviceId(jsonNodeHttpResponse.getBody().getObject().get("id").toString()))
                .ifFailure(jsonNodeHttpResponse -> {
                    logger.error("Oh No! Status {} , {}", jsonNodeHttpResponse.getStatus(), jsonNodeHttpResponse.getStatusText());
                    jsonNodeHttpResponse.getParsingError().ifPresent(exception -> {
                        logger.error("Parsing Exception: ", exception);
                        logger.error("Original body: {}", exception.getOriginalBody());
                    });
                });
    }

    /**
     * Update a device's credentials
     *
     * @param resourcePath route parameter "/v1/credentials/{tenantId}/{deviceId}"
     */
    public void setDevicePassword(String resourcePath) {
        Unirest.put(resourcePath)
                .header("content-type", "application/json")
                .body(String.format("[{ \"type\": \"hashed-password\", \"auth-id\": \"%s\", \"secrets\": [{\"pwd-plain\": \"%s\" }] }]", getDeviceId(), this.devicePassword))
                .asEmpty().ifSuccess(emptyHttpResponse -> logger.info("Password is set!"))
                .ifFailure(emptyHttpResponse -> {
                    logger.error("Unable to set password! Status {} , {}", emptyHttpResponse.getStatus(), emptyHttpResponse.getStatusText());
                    emptyHttpResponse.getParsingError().ifPresent(exception -> {
                        logger.error("Parsing Exception: ", exception);
                        logger.error("Original body: {}", exception.getOriginalBody());
                    });
                });
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
