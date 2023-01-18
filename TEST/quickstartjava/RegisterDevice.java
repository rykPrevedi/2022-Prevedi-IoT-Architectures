package it.unimore.tirocinio.prevedi.iot.architectures.quickstartjava;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Riccardo Prevedi
 * @created 16/01/2023 - 17:29
 * @project iot-architectures
 */

public class RegisterDevice {

    private static final Logger logger = LoggerFactory.getLogger(RegisterDevice.class);
    private String registryUrl = null;   //"http://192.168.181.13:28080"
    private String tenantId = null;
    private String deviceId = null;

    public RegisterDevice(String registryBaseUrl) {
        this.registryUrl = registryBaseUrl;
        // Configuring a default base URL to be used for all requests that do not contain a full URL
        Unirest.config().defaultBaseUrl(this.registryUrl);
    }

    public static void main(String[] args) {

        // Adding a new tenant to the Device Registry
        RegisterDevice deviceRegistry = new RegisterDevice("http://192.168.181.13:1080");

        // Register Tenant
        deviceRegistry.createTenant("/v1/tenants");
        System.out.println("Registered tenant: " + deviceRegistry.getTenantId());

        // Add Device to Tenant
        deviceRegistry.addDevice("/v1/devices");
        System.out.println("Registered device: " + deviceRegistry.getDeviceId());

        // Set Device Password
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
                .header("accept", "application/json")
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
     * @param resourcePath route parameter "/v1/devices"
     */
    public void addDevice(String resourcePath) {

        HttpResponse<JsonNode> device = Unirest.post(resourcePath + "/{tenantId}")
                .routeParam("tenantId", this.tenantId)
                .header("content-type", "application/json")
                .asJson();

        if (device.isSuccess())
            setDeviceId(device.getBody().getObject().get("id").toString());
        else
            logger.error("Oh No! Status {}", device.getStatus());
            device.getParsingError().ifPresent(e -> {
                logger.error("Parsing Exception: ", e);
                logger.error("Original body: {}", e.getOriginalBody());
            });
    }

    public void setPassword(String resourcePath, String password){
        Unirest.put()

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
