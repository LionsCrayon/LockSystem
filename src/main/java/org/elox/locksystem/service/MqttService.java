package org.elox.locksystem.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttService {
    @Autowired
    private MqttClient mqttClient;

    public void sendCommandToDevice(String requestId, Long deviceId) {
        String topic = "lock/" + deviceId + "/command";
        String payload = String.format("{\"requestId\":\"%s\",\"command\":\"open\"}", requestId);
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        try {
            mqttClient.publish(topic, message);
            log.info("MQTT message sent to topic: {}, payload: {}", topic, payload);
        } catch (MqttException e) {
            log.error("Failed to send MQTT message", e);
        }
    }
}