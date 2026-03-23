package org.elox.locksystem.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.elox.locksystem.dto.OpenLockResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketMQProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送开锁指令
    public void sendCommand(String requestId, Long userId, Long deviceId) {
        String payload = String.format("{\"requestId\":\"%s\",\"userId\":%d,\"deviceId\":%d,\"command\":\"open\"}", requestId, userId, deviceId);
        rocketMQTemplate.send("open-lock-command", MessageBuilder.withPayload(payload).build());
    }

    // 发送开锁结果
    public void sendResult(OpenLockResult result) {
        try {
            rocketMQTemplate.syncSend("open-lock-result", result);
            log.info("syncSend success. destination:open-lock-result, message: {}", result);
        } catch (Exception e) {
            log.error("Failed to send result to RocketMQ", e);
        }
//        rocketMQTemplate.send("open-lock-result", MessageBuilder.withPayload(result).build());
    }
}
