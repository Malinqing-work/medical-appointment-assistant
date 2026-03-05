# 🏥 医疗预约助手系统


基于 Spring Boot 和 LangChain4j 构建的智能医疗预约服务平台，提供自然语言交互的预约挂号、科室医生查询等功能。

## 📋 项目特性

### 🤖 AI 智能助手
- 基于 LangChain4j 框架集成大语言模型
- 支持阿里通义千问模型接入
- 实现多轮对话上下文记忆
- 持久化聊天历史存储 (MongoDB)
- 智能意图识别和工具调用

### 🏥 医疗预约服务
- **预约挂号**: 患者信息录入、医生科室选择、时间段预约
- **预约管理**: 取消预约、查询预约状态
- **科室管理**: 多科室信息维护与查询
- **医生排班**: 医生信息查询、排班时间表管理

### 📊 数据管理
- MySQL 存储业务数据 (预约、科室、医生信息)
- MongoDB 存储对话记忆和向量数据
- MyBatis-Plus 实现高效数据访问
- 向量检索增强 (RAG) 支持知识库查询

### 🔧 开发运维
- RESTful API 接口设计
- Knife4j OpenAPI3 接口文档自动生成
- Maven 依赖统一管理
- 多环境配置支持

## 🏗️ 技术架构

```
├── 应用层
│   ├── HospitalApp.java                 # Spring Boot 启动入口
│   └── assistant.Assistant              # AI 对话接口
├── 服务层
│   ├── service.*Service                # 业务服务接口
│   └── service.impl.*ServiceImpl       # 业务服务实现
├── 数据层
│   ├── mapper.*Mapper                  # MyBatis 映射接口
│   ├── domain.*                       # 实体类
│   └── store.PersistentChatMemoryStore  # 持久化存储
├── 工具层
│   ├── tools.HospitalTools             # 预约相关工具
│   └── config.AssistantConfig          # AI 配置
└── 前端层 
    └── hospital-ui/                    # Vue.js 前端应用
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- MongoDB 6.0+
- 阿里通义千问 API Key

### 数据库配置

1. **MySQL 数据库初始化**

创建数据库：
```sql
CREATE DATABASE hospital_agent CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **MongoDB 配置**
确保 MongoDB 服务正常运行，用于存储对话记忆。

### 应用配置

编辑 `hospital-agent/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hospital_agent?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password

  data:
    mongodb:
      uri: mongodb://localhost:27017/hospital_agent

langchain4j:
  dashscope:
    api-key: your_dashscope_api_key
    model-name: qwen-max
```

### 构建与运行

```bash
# 1. 克隆项目
git clone https://github.com/Malinqing-work/medical-appointment-assistant.git
cd medical-appointment-assistant

# 2. 构建项目
mvn clean package -DskipTests

# 3. 运行应用
java -jar hospital-agent/target/hospital-agent-1.0-SNAPSHOT.jar

# 或使用 Maven 运行
mvn spring-boot:run
```

应用启动后访问：http://localhost:8080

### API 文档

启动应用后访问 Knife4j 接口文档：
http://localhost:8080/doc.html

## 📖 开发指南

### 项目结构

```
hospital-agent/
├── app/
│   ├── assistant/          # AI 助手接口
│   ├── config/             # 配置类
│   ├── domain/             # 实体类
│   ├── mapper/             # MyBatis 映射
│   ├── service/            # 服务层
│   ├── store/              # 持久化存储
│   ├── tools/              # 工具类
│   └── HospitalApp.java    # 启动类
├── resources/
│   ├── application.yml     # 配置文件
│   └── my-prompt-template.txt  # AI 提示词模板
└── pom.xml                 # Maven 配置
```

### 核心接口

#### AI 对话接口
```java
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        tools = {"hospitalTools", "mcpToolProvider"},
        contentRetriever = "contentRetriever")
public interface Assistant {
    @SystemMessage(fromResource = "my-prompt-template.txt")
    Flux<String> chat(@MemoryId String messageId, @UserMessage("message") String message);
}
```

#### 预约服务接口
```java
public interface AppointmentService {
    boolean save(Appointment appointment);
    boolean remove(String name, String idCard, String time);
    Appointment getOne(String name, String idCard, String time);
    Appointment getOne(String name, String idCard, String department, String doctor, String time);
}
```

### 工具调用

系统通过 LangChain4j 的 `@Tool` 注解暴露以下功能：

1. **预约挂号**: `appointment(name, idCard, department, doctor, time)`
2. **取消预约**: `cancelAppointment(idCard, name, time)`
3. **查询科室**: `queryDepartment()`
4. **查询医生**: `queryDoctor()`
5. **查询排班**: `querySchedule(doctorName)`
6. **查询科室医生**: `queryDepartmentDoctor(departmentName)`

### 数据库表结构

```sql
-- 预约表
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(20) COMMENT '患者姓名',
    id_card VARCHAR(20) COMMENT '身份证号',
    department VARCHAR(20) COMMENT '科室',
    doctor VARCHAR(20) COMMENT '医生',
    time VARCHAR(20) COMMENT '预约时间',
    INDEX idx_id_card (id_card),
    INDEX idx_time (time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约挂号表';

-- 科室表 (需根据实际业务补充)
-- 医生表 (需根据实际业务补充)
-- 排班表 (需根据实际业务补充)
```

## 🔧 配置说明

### AI 模型配置

支持配置不同的 LLM 模型：
```yaml
langchain4j:
  dashscope:
    api-key: ${DASHSCOPE_API_KEY}
    model-name: qwen-max
    temperature: 0.7
    max-tokens: 2000
```

### 向量检索配置

```java
@Bean
public EmbeddingStore store() {
    // 加载文档
    List<Document> documents = ClassPathDocumentLoader.loadDocuments("docs");
    // 构建向量存储
    InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
    // 文档分割和向量化
    DocumentSplitter ds = DocumentSplitters.recursive(300, 50);
    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .embeddingStore(store)
            .documentSplitter(ds)
            .embeddingModel(embeddingModel)
            .build();
    ingestor.ingest(documents);
    return store;
}
```

### 对话记忆配置

```java
@Bean
public ChatMemoryProvider chatMemoryProvider() {
    return memoryId -> MessageWindowChatMemory.builder()
            .id(memoryId)
            .maxMessages(40)  // 记忆窗口大小
            .chatMemoryStore(persistentChatMemoryStore)
            .build();
}
```

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify -Pintegration
```

## 📦 部署

### Docker 部署

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY hospital-agent/target/hospital-agent-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# 构建镜像
docker build -t hospital-agent:1.0 .

# 运行容器
docker run -d \
  --name hospital-agent \
  -p 8080:8080 \
  -v /path/to/config:/app/config \
  hospital-agent:1.0
```

### Kubernetes 部署

参考 Kubernetes Deployment 配置示例，包含 ConfigMap、Secret 等资源配置。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进项目。

## 📄 许可证

本项目采用 [MIT License](LICENSE)。

## 📞 联系方式

如有问题或建议，请提交 GitHub Issue。

---

**注意**: 本项目为演示项目，生产环境部署前请进行充分的安全性和性能测试。
