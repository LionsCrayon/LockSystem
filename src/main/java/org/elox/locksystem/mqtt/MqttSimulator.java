package org.elox.locksystem.mqtt;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.elox.locksystem.dto.OpenLockResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Slf4j
@Component
public class MqttSimulator implements MqttCallback {

    @Autowired
    @Qualifier("mqttClientForSimulator")
    private MqttClient mqttClient;

    @Autowired
    private ObjectMapper objectMapper;
    private final Random random = new Random();

    @PostConstruct
    public void init() throws MqttException {
        mqttClient.setCallback(this);
        mqttClient.subscribe("lock/+/command", 1);
        log.info("MCU simulator subscribed to lock/+/command");
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("MCU simulator connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 只处理以 "/command" 结尾的主题，忽略其他（如 result）
        if (!topic.endsWith("/command")) {
            log.debug("Ignoring non-command message on topic: {}", topic);
            return;
        }

        String payload = new String(message.getPayload());
        log.info("MCU simulator received command: topic={}, payload={}", topic, payload);

        // 解析 topic 获取 deviceId
        String[] parts = topic.split("/");
        String deviceId = parts[1];

        // 解析 requestId
        JsonNode root = objectMapper.readTree(payload);
        String requestId = root.get("requestId").asText();

        // 模拟开锁过程
        Thread.sleep(500);
        boolean success = random.nextBoolean();
        String resultMessage = success ? "Door opened" : "Failed to open door";

        // 构造结果并发送到 result 主题
        String resultTopic = "lock/" + deviceId + "/result";
        String resultPayload = objectMapper.writeValueAsString(
                new OpenLockResult(requestId, success, resultMessage)
        );
        MqttMessage resultMsg = new MqttMessage(resultPayload.getBytes());
        resultMsg.setQos(1);
        mqttClient.publish(resultTopic, resultMsg);
        log.info("MCU simulator sent result: topic={}, payload={}", resultTopic, resultPayload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}