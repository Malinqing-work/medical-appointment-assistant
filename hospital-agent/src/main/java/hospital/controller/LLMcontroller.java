package hospital.controller;


import hospital.assistant.Assistant;
import hospital.entity.ChatMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/agent")
public class LLMcontroller {
    @Autowired
    private Assistant assistant;
    @PostMapping (value = "/chatAgent",produces = "text/html;charset=utf-8")
    // Get Post Put Delete 请求都是相对后端持久化的数据而言
    public Flux<String> test(@RequestBody ChatMessages chatMessages) {
        return assistant.chat(String.valueOf(chatMessages.getMessageId()),chatMessages.getMessage());
    }
}
