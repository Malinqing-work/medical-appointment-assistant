package com.hospital.app.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

//
//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
//        streamingChatModel = "qwenStreamingChatModel",
//        chatMemoryProvider = "chatMemoryProvider",tools = {"hospitalTools", "mcpToolProvider"},
//        contentRetriever = "contentRetriever")
public interface Assistant {
    @SystemMessage(fromResource = "my-prompt-template.txt")
    Flux<String> chat(@MemoryId String messageId , @UserMessage("message") String  message);
}
