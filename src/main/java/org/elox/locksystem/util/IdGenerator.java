package org.elox.locksystem.util;


import java.util.UUID;

public class IdGenerator {
    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}