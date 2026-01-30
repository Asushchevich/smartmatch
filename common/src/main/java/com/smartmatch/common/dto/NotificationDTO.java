package com.smartmatch.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Serializable {
    private String type;
    private String message;
}
