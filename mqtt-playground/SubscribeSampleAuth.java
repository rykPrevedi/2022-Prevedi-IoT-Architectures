package it.unimore.tirocinio.emqx.mqtt.client;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Simple MQTT Subscriber using the library Eclipse Paho
 *
 * @author Riccardo Prevedi
 * @created 27/12/2022 - 14:53
 * @project MQTT-client-java
 */

public class SubscribeSampleAuth {

    private static final Logger logger = LoggerFactory.getLogger(PublishSampleAuth.class);
    private static final String broker = "tcp://localhost:1883";
    private static final String username = "riccardo";
    private static final String password = "olacico";
    private static final String clientId = "subscribe_client";
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
            // setup callback
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("topic: " + topic);
                    System.out.println("Qos: " + message.getQos());
                    System.out.println("message content: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
            mqttClient.connect(options);
            logger.info("Connected ! clientId: {}", clientId);
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
