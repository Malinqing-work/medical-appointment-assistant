package com.hospital.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessages {

    private String memoryId; //聊天ID
    private String message; //聊天内容
}
