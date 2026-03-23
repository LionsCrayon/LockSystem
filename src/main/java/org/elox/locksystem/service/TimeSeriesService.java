package org.elox.locksystem.service;

import lombok.extern.slf4j.Slf4j;
import org.elox.locksystem.entity.LockRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeSeriesService {
    public void recordOpenLock(LockRecord record) {
        // 实际中这里会写入InfluxDB、TimescaleDB等
        log.info("TimeSeries record: deviceId={}, userId={}, time={}, status={}",
                record.getDeviceId(), record.getUserId(), record.getCompleteTime(), record.getStatus());
    }
}