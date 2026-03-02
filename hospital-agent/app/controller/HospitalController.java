package com.hospital.app.controller;

import com.hospital.app.assistant.Assistant;
import com.hospital.app.domain.ChatMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/agent")
public class HospitalController {

    @Autowired
    private Assistant Assistant;

    @PostMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatMessages chatMessages) {
        return Assistant.chat(chatMessages.getMemoryId(),chatMessages.getMessage());
    }
}
