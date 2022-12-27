package it.unimore.tirocinio.emqx.mqtt.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Simple MQTT Producer using the library Eclipse Paho
 *
 * @author Riccardo Prevedi
 * @created 26/12/2022 - 17:56
 * @project MQTT-client-java
 */

public class PublishSampleAuth {

    private static final Logger logger = LoggerFactory.getLogger(PublishSampleAuth.class);
    private static final String broker = "tcp://localhost:1883";
    private static final String username = "riccardo";
    private static final String password = "olacico";
    private static final String clientId = "publish_client";
    private static final String payload = "Hello MQTT";
    private static final String topic = "mqtt/test";
    private static final int qos = 0;

    public static void main(String[] args) {
        logger.info("test client started...");
        try {
            IMqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            // connect options
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // connect
            mqttClient.connect(options);
            logger.info("Connected ! clientId: {}", clientId);
            // create message and setup QoS, retain
            MqttMessage msg = new MqttMessage(payload.getBytes());
            msg.setRetained(false);
            msg.setQos(qos);
            // publish
            mqttClient.publish(topic, msg);
            System.out.println("Message published");
            System.out.println("topic: " + topic);
            System.out.println("message content: " + payload);
            // disconnect
            mqttClient.disconnect();
            // close client
            mqttClient.close();
            logger.info("Disconnected ! clientId: {}", clientId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
