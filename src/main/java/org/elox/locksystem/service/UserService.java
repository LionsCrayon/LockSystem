package org.elox.locksystem.service;

import org.elox.locksystem.dao.UserDao;
import org.elox.locksystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Cacheable(value = "user", key = "#userId")
    public User getUserById(Long userId) {
        return userDao.findById(userId);
    }
}
