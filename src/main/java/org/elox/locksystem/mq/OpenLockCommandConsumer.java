package org.elox.locksystem.mq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.elox.locksystem.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "open-lock-command", consumerGroup = "open-lock-command-group")
public class OpenLockCommandConsumer implements RocketMQListener<String> {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(String message) {
        log.info("Received open-lock-command: {}", message);
        try {
            JsonNode root = objectMapper.readTree(message);
            String requestId = root.get("requestId").asText();
            Long deviceId = root.get("deviceId").asLong();
            mqttService.sendCommandToDevice(requestId, deviceId);
        } catch (Exception e) {
            log.error("Failed to process command message", e);
            throw new RuntimeException(e); // 让 RocketMQ 重试
        }
    }
}
