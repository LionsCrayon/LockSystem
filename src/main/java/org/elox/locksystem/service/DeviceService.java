package org.elox.locksystem.service;

import org.elox.locksystem.dao.DeviceDao;
import org.elox.locksystem.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    private DeviceDao deviceDao;

    @Cacheable(value = "device", key = "#deviceId")
    public Device getDeviceById(Long deviceId) {
        return deviceDao.findById(deviceId);
    }
}