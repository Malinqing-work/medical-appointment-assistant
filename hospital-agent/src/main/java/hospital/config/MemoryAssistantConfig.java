package hospital.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import hospital.store.PersistentChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryAssistantConfig {
    @Autowired
    private PersistentChatMemoryStore persistentChatMemoryStore;
    @Bean
    // 表明了智能体的行为 通过配置来声明
    public ChatMemoryProvider ChatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId) //实现聊天的隔离性
                .chatMemoryStore(persistentChatMemoryStore) // 将聊天内容持久化
                .maxMessages(10)  // 记忆窗口大小
                .build();

    }
}
