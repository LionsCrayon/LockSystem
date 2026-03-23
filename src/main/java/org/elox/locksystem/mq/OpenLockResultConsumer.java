package org.elox.locksystem.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.elox.locksystem.dto.OpenLockResult;
import org.elox.locksystem.service.LockRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "open-lock-result", consumerGroup = "open-lock-result-group")
public class OpenLockResultConsumer implements RocketMQListener<OpenLockResult> {

    @Autowired
    private LockRecordService lockRecordService;

    @Override
    public void onMessage(OpenLockResult result) {
        log.info("Received open-lock-result: {}", result);
        try {
            lockRecordService.updateRecordResult(result.getRequestId(), result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            log.error("Error processing result for requestId: " + result.getRequestId(), e);
            // 可以选择抛出异常让 RocketMQ 重试，或者根据业务决定是否重试
            throw e;
        }
    }
}