package org.elox.locksystem.controller;


import org.elox.locksystem.dao.LockRecordDao;
import org.elox.locksystem.dto.OpenLockRequest;
import org.elox.locksystem.entity.Device;
import org.elox.locksystem.entity.LockRecord;
import org.elox.locksystem.entity.User;
import org.elox.locksystem.service.CommandService;
import org.elox.locksystem.service.DeviceService;
import org.elox.locksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LockController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CommandService commandService;
    @Autowired
    private LockRecordDao lockRecordDao;

    @PostMapping("/open-lock")
    public ResponseEntity<?> openLock(@RequestBody OpenLockRequest request) {
        User user = userService.getUserById(request.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Device device = deviceService.getDeviceById(request.getDeviceId());
        if (device == null) {
            return ResponseEntity.badRequest().body("Device not found");
        }
        String requestId = commandService.sendOpenCommand(request.getUserId(), request.getDeviceId());
        return ResponseEntity.accepted().body("Request accepted, requestId: " + requestId);
    }

    @GetMapping("/record/{requestId}")
    public ResponseEntity<?> getRecord(@PathVariable String requestId) {
        try {
            LockRecord record = lockRecordDao.findById(requestId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
