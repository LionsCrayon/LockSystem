package org.elox.locksystem.service;

import org.elox.locksystem.dao.LockRecordDao;
import org.elox.locksystem.entity.LockRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LockRecordService {
    @Autowired
    private LockRecordDao lockRecordDao;
    @Autowired
    private TimeSeriesService timeSeriesService;

    public LockRecord createPendingRecord(String requestId, Long userId, Long deviceId) {
        LockRecord record = new LockRecord();
        record.setRequestId(requestId);
        record.setUserId(userId);
        record.setDeviceId(deviceId);
        record.setRequestTime(LocalDateTime.now());
        record.setStatus("PENDING");
        lockRecordDao.insert(record);
        return record;
    }

    public void updateRecordResult(String requestId, boolean success, String message) {
        LocalDateTime completeTime = LocalDateTime.now();
        lockRecordDao.updateResult(requestId, success, message, completeTime);
        // 模拟时序数据库记录
        LockRecord record = lockRecordDao.findById(requestId);
        timeSeriesService.recordOpenLock(record);
    }
}
