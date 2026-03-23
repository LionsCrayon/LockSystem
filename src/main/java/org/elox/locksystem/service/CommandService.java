package org.elox.locksystem.service;

import org.elox.locksystem.mq.RocketMQProducer;
import org.elox.locksystem.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandService {
    @Autowired
    private RocketMQProducer rocketMQProducer;
    @Autowired
    private LockRecordService lockRecordService;

    public String sendOpenCommand(Long userId, Long deviceId) {
        String requestId = IdGenerator.generateRequestId();
        // 创建待处理记录
        lockRecordService.createPendingRecord(requestId, userId, deviceId);
        // 发送到RocketMQ
        rocketMQProducer.sendCommand(requestId, userId, deviceId);
        return requestId;
    }
}
