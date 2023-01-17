package it.unimore.tirocinio.prevedi.iot.architectures.quickstartjava;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Riccardo Prevedi
 * @created 16/01/2023 - 17:29
 * @project iot-architectures
 */

public class RegisterDevice {

    private static final Logger logger = LoggerFactory.getLogger(RegisterDevice.class);
    private String registryUrl = null;   //"http://192.168.181.13:32774"

    public RegisterDevice(String registryBaseUrl) {
        this.registryUrl = registryBaseUrl;
        // Configuring a default base URL to be used for all requests that do not contain a full URL
        Unirest.config().defaultBaseUrl(this.registryUrl);
    }

    public static void main(String[] args) {

        // Adding a new tenant to the Device Registry
        RegisterDevice deviceRegistry = new RegisterDevice("http://192.168.181.13:32774");

        // Register Tenant
        String tenantId = deviceRegistry.createTenant("/v1/tenants");
        System.out.println("Registered tenant: " + tenantId);

        // Add Device to Tenant
        String deviceId = deviceRegistry.addingDevice("/v1/devices", tenantId);
        System.out.println("Registered device: " + deviceId);

        // Set Device Password
    }

    /**
     * Create a new device registration with auto-generated ID
     *
     * @param resourcePath route parameter "/v1/devices"
     * @param tenantId     route parameter added dynamically
     * @return tenant ID
     */
    public String addingDevice(String resourcePath, String tenantId) {

        // Making the POST Request. Requests are made when as[Type]() is invoked.
        // Sometimes you want to add dynamic parameters in the URL
        HttpResponse<JsonNode> device = Unirest.post(resourcePath + "/{tenantId}")
                .routeParam("tenantId", tenantId)
                .header("content-type", "application/json")
                .asJson();

        if (device.getBody() != null && device.isSuccess())  // true if the response was a 200-series
        {
            // Extract the value from the key "id" of the tenant Object
            return device.getBody().getObject().get("id").toString();

        } else {

            // the asJson request BODY could be null.
            // Get the error and the original body for inspection.
            UnirestParsingException ex = device.getParsingError().get();

            logger.error("""


                    Error executing the request !

                    Original body: {}
                    Status Code: {}
                    Exception occurred : {}
                    """, ex.getOriginalBody(), device.getStatus(), ex.getMessage());
            return null;
        }
    }


    /**
     * Create a new tenant with an auto-generated-ID
     *
     * @param resourcePath route parameter "/v1/tenants"
     * @return tenant ID
     */
    public String createTenant(String resourcePath) {

        // Making the POST Request. Requests are made when as[Type]() is invoked,
        HttpResponse<JsonNode> tenant = Unirest.post(resourcePath)
                .header("content-type", "application/json")
                .body("{\"ext\": {\"messaging-type\": \"amqp\"}}")
                .asJson();

        if (tenant.getBody() != null && tenant.isSuccess())  // true if the response was a 200-series
        {
            // Extract the value from the key "id" of the tenant Object
            return tenant.getBody().getObject().get("id").toString();

        } else {
            // the asJson request BODY could be null.
            // Get the error and the original body for inspection.
            UnirestParsingException ex = tenant.getParsingError().get();

            logger.error("""


                    Error executing the request !

                    Original body: {}
                    Status Code: {}
                    Exception occurred : {}
                    """, ex.getOriginalBody(), tenant.getStatus(), ex.getMessage());
            return null;
        }
    }
}
