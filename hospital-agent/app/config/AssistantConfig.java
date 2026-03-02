package com.hospital.app.config;

import com.hospital.app.store.PersistentChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AssistantConfig {

    @Autowired
    private PersistentChatMemoryStore persistentChatMemoryStore;
    @Autowired
    private EmbeddingModel embeddingModel;
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)  // 每个会话的记忆ID,必须是唯一的
                .maxMessages(40) // 每个会话的记忆长度
                .chatMemoryStore(persistentChatMemoryStore) // 使用持久化记忆
                .build(); // 创建一个消息窗口记忆
    }

    @Bean
    public EmbeddingStore store(){
        // 加载文档进内存
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("docs");
        //构建向量数据库操作对象
        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
        //构建文档分割器对象
        DocumentSplitter ds = DocumentSplitters.recursive(300, 50);
        //构建EmbeddingStoreIngestor对象, 完成文本数据切割向量化并保存进向量数据库
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .documentSplitter(ds)
                .embeddingModel(embeddingModel)
                .build();
        ingestor.ingest(documents);
        return store;
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore store){
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .minScore(0.7)
                .maxResults(5)
                .embeddingModel(embeddingModel)
                .build();
    }
}