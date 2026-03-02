package com.hospital.app.assistant;


import com.hospital.app.tools.HospitalTools;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeServiceFactory {
    @Resource
    private StreamingChatLanguageModel qwenStreamingChatModel;
    @Resource
    private ChatMemoryProvider chatMemoryProvider;
    @Resource
    private McpToolProvider mcpToolProvider;
    @Resource
    private ContentRetriever contentRetriever;
    @Resource
    private HospitalTools hospitalTools;

    @Bean
    public Assistant aiCodeService(){
        Assistant as = AiServices.builder(Assistant.class)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(hospitalTools)
                .toolProvider(mcpToolProvider)
                .contentRetriever(contentRetriever)
                .build();
        return as;
    }
}
