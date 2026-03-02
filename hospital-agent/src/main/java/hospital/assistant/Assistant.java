package hospital.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

// 通过注解将其声明为Spring的一个组件  AIservice表示一个智能组件
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "ChatMemoryProvider")
public interface Assistant {
    @SystemMessage(fromResource="my-prompt-template.txt")
    Flux<String> chat(@MemoryId String memoryId, @UserMessage  String userMessage); // 接口中定义抽象类即可
}
