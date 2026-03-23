package org.elox.locksystem.mqtt;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.elox.locksystem.dto.OpenLockResult;
import org.elox.locksystem.mq.RocketMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MqttResultListener implements MqttCallback {

    @Autowired
    @Qualifier("mqttClientForListener")
    private MqttClient mqttClient;
    @Autowired
    private RocketMQProducer rocketMQProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws MqttException {
        mqttClient.setCallback(this);
        mqttClient.subscribe("lock/+/result", 1);
        log.info("Subscribed to lock/+/result");
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        log.info("Received MQTT result: topic={}, payload={}", topic, payload);
        // 转换为OpenLockResult
        OpenLockResult result = objectMapper.readValue(payload, OpenLockResult.class);
        // 转发到RocketMQ
        rocketMQProducer.sendResult(result);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // 无需处理
    }
}