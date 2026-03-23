package org.elox.locksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenLockResult  {
    private String requestId;
    private boolean success;
    private String message;
}
