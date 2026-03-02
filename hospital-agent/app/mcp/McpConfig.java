package com.hospital.app.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Value("${bigmodel.api-key}")
    public String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider() {
        //与mcp创建连接
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://mcp.amap.com/sse?key="+apiKey)
                .logRequests(true)
                .logResponses(true)
                .build();
        //利用连接创建mcp客户端
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        //从客户端中取到mcp服务提供的工具
        McpToolProvider toolProvider = new McpToolProvider.Builder()
                .mcpClients(mcpClient)
                .build();

        return toolProvider;
    }
}
