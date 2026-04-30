# OpsPilot 技术方案设计

## 1. 项目定位

### 1.1 项目名称

OpsPilot：面向微服务研发团队的智能运维排障 Agent。

### 1.2 项目背景

在微服务系统中，线上问题排查通常需要研发人员同时查看应用日志、监控指标、发布记录、Git 变更、故障手册和历史工单。信息分散在多个系统中，排障过程重复且高度依赖个人经验。

OpsPilot 通过 Agent 编排、多工具调用和 RAG 知识检索，自动聚合多源证据，生成可追溯的排障结论和处理建议。

### 1.3 项目目标

- 理解用户输入的自然语言排障请求。
- 自动生成排障计划。
- 调用日志、监控、Git 变更、知识库、工单等工具。
- 收集并标准化证据。
- 生成可追溯的排障报告。
- 记录任务步骤、工具调用和执行审计日志。
- 使用本地 demo 数据支持可复现演示。
- 在后续版本中支持高风险操作的人工确认机制。

### 1.4 非目标

- 不做通用 AIOps 平台。
- 不使用真实公司生产数据。
- 不允许 LLM 直接执行高风险操作。
- 不涉及模型训练、微调或复杂多 Agent 研究能力。

## 2. 技术栈

### 2.1 MVP 技术栈

- Java 17
- Spring Boot 3.x
- Maven
- H2 或 PostgreSQL
- 本地仿真数据
- 规则 Planner
- 关键词知识库检索
- Swagger 或 Knife4j
- JUnit 5

### 2.2 目标技术栈

- Java 17
- Spring Boot 3.x
- Maven
- LangChain4j
- PostgreSQL
- pgvector
- Redis
- Server-Sent Events
- Docker Compose
- JUnit 5 + Mockito

### 2.3 可选集成

- Elasticsearch：用于日志检索。
- Prometheus：用于监控指标查询。
- GitLab 或 GitHub：用于代码变更分析。
- Jira 或本地工单系统：用于工单分析。
- Kafka 或 RabbitMQ：用于异步任务执行。

## 3. 总体架构

```text
用户 / 前端 / Swagger
        |
        v
API 接入层
        |
        v
Agent 编排层
        |
        +----------------+
        |                |
        v                v
工具调用层          RAG 知识库层
        |                |
        v                v
本地仿真数据       PostgreSQL + pgvector
日志/监控/Git      文档/Chunk/Embedding
        |
        v
证据汇总 + 答案生成
        |
        v
SSE 实时推送 / 任务结果查询
```

运行时结构：

```text
                 +----------------------+
                 | AgentController      |
                 +----------+-----------+
                            |
                            v
                 +----------------------+
                 | AgentOrchestrator    |
                 +----------+-----------+
                            |
        +-------------------+-------------------+
        |                   |                   |
        v                   v                   v
+----------------+  +----------------+  +------------------+
| PlanningService|  | ToolExecutor   |  | TaskStateMachine |
+----------------+  +-------+--------+  +------------------+
                            |
                            v
                 +----------------------+
                 | ToolRegistry         |
                 +----------+-----------+
                            |
        +-------------------+-------------------+
        |                   |                   |
        v                   v                   v
+---------------+   +---------------+   +----------------+
| LogSearchTool |   | MetricTool    |   | KnowledgeTool  |
+---------------+   +---------------+   +----------------+
        |                   |                   |
        v                   v                   v
本地日志              本地监控数据         文档 / pgvector
```

## 4. 模块分层

推荐包结构：

```text
com.opspilot
├── OpsPilotApplication.java
│
├── api
│   ├── AgentController.java
│   ├── KnowledgeController.java
│   └── TicketController.java
│
├── application
│   ├── AgentApplicationService.java
│   ├── KnowledgeApplicationService.java
│   └── TicketApplicationService.java
│
├── agent
│   ├── orchestrator
│   │   ├── AgentOrchestrator.java
│   │   ├── TaskStateMachine.java
│   │   └── AgentEventPublisher.java
│   │
│   ├── planning
│   │   ├── PlanningService.java
│   │   ├── RuleBasedPlanningService.java
│   │   └── LlmPlanningService.java
│   │
│   ├── execution
│   │   ├── ToolExecutionService.java
│   │   ├── ToolRegistry.java
│   │   └── ToolExecutionContext.java
│   │
│   ├── memory
│   │   ├── ConversationMemoryService.java
│   │   └── TaskMemoryService.java
│   │
│   └── answer
│       ├── AnswerComposer.java
│       └── EvidenceCollector.java
│
├── tool
│   ├── core
│   │   ├── OpsTool.java
│   │   ├── ToolRequest.java
│   │   ├── ToolResult.java
│   │   ├── ToolDefinition.java
│   │   └── ToolRiskLevel.java
│   │
│   ├── log
│   │   ├── LocalLogSearchTool.java
│   │   └── ElasticsearchLogSearchTool.java
│   │
│   ├── metric
│   │   ├── LocalMetricQueryTool.java
│   │   └── PrometheusMetricQueryTool.java
│   │
│   ├── git
│   │   ├── LocalGitChangeTool.java
│   │   └── GitLabChangeTool.java
│   │
│   ├── knowledge
│   │   └── KnowledgeSearchTool.java
│   │
│   └── ticket
│       ├── LocalTicketTool.java
│       └── TicketSearchTool.java
│
├── rag
│   ├── DocumentIngestionService.java
│   ├── DocumentChunker.java
│   ├── EmbeddingService.java
│   ├── VectorSearchService.java
│   └── RetrievalAugmentor.java
│
├── domain
│   ├── task
│   │   ├── AgentTask.java
│   │   ├── AgentStep.java
│   │   ├── AgentTaskStatus.java
│   │   └── AgentStepStatus.java
│   │
│   ├── evidence
│   │   ├── Evidence.java
│   │   └── EvidenceSourceType.java
│   │
│   ├── approval
│   │   ├── ApprovalRecord.java
│   │   └── ApprovalStatus.java
│   │
│   └── knowledge
│       ├── KnowledgeDocument.java
│       └── KnowledgeChunk.java
│
├── infrastructure
│   ├── repository
│   ├── llm
│   ├── sse
│   ├── config
│   └── demo
│
└── common
    ├── exception
    ├── response
    └── util
```

## 5. 核心模块设计

### 5.1 API 接入层

API 层负责对外暴露任务创建、任务查询、执行事件推送和知识库管理接口。

计划接口：

```http
POST /api/agent/tasks
GET /api/agent/tasks/{taskId}
GET /api/agent/tasks/{taskId}/events
POST /api/knowledge/documents
POST /api/tickets/{ticketId}/analyze
```

MVP 接口：

```http
POST /api/agent/tasks
GET /api/agent/tasks/{taskId}
GET /api/agent/tasks/{taskId}/events
```

### 5.2 Agent 编排层

Agent 编排层是项目核心。它负责把一次用户请求转换成一个有状态的 Agent 任务。

执行流程：

```text
接收用户问题
-> 创建 agent_task
-> 识别意图
-> 生成计划
-> 执行工具
-> 收集证据
-> 生成最终答案
-> 更新任务状态
-> 推送执行事件
```

任务状态：

```text
CREATED
INTENT_RECOGNIZED
PLAN_GENERATED
TOOL_EXECUTING
EVIDENCE_COLLECTED
WAITING_APPROVAL
ANSWER_GENERATED
FAILED
```

步骤状态：

```text
PENDING
RUNNING
SUCCESS
FAILED
SKIPPED
WAITING_APPROVAL
```

### 5.3 PlanningService

MVP 阶段使用规则 Planner，保证第一版确定、可测、容易演示。

规则示例：

```text
问题包含“500”、“报错”、“异常”
-> INCIDENT_5XX_DIAGNOSIS

问题包含“慢”、“超时”、“RT”、“耗时”
-> PERFORMANCE_DIAGNOSIS

问题包含“ticket”或“工单”
-> TICKET_ANALYSIS
```

`INCIDENT_5XX_DIAGNOSIS` 的计划示例：

```text
1. 查询错误日志。
2. 查询错误率监控。
3. 查询故障时间附近的 Git 变更。
4. 检索故障处理手册。
5. 生成排障报告。
```

后续可以增加 `LlmPlanningService`，由模型生成 JSON 格式执行计划，后端负责校验工具名、参数和风险等级。

### 5.4 工具调用层

所有工具通过统一适配器接口暴露：

```java
public interface OpsTool {
    ToolDefinition definition();
    ToolResult execute(ToolRequest request);
}
```

每个工具需要定义：

- 工具名称
- 工具描述
- 参数 schema
- 风险等级
- 超时时间
- 是否需要人工确认
- 执行逻辑

MVP 工具：

- `LocalLogSearchTool`
- `LocalMetricQueryTool`
- `LocalKnowledgeSearchTool`
- `LocalGitChangeTool`

后续真实系统适配器：

- `ElasticsearchLogSearchTool`
- `PrometheusMetricQueryTool`
- `GitLabChangeTool`
- `JiraTicketTool`

编排层只依赖 `OpsTool` 接口，因此本地仿真数据可以平滑替换为真实外部系统。

### 5.5 RAG 知识库层

知识库层负责检索故障手册和技术文档，为排障结论提供依据。

目标流程：

```text
上传 Markdown 文档
-> 清洗文本
-> 切分 Chunk
-> 生成 Embedding
-> 存入 pgvector
-> 对用户问题生成 Embedding
-> 检索 TopK 相关 Chunk
-> 将检索结果注入最终答案 Prompt
```

知识库文档示例：

- Redis 超时排查 SOP
- 慢 SQL 优化规范
- HTTP 500 排查手册
- 发布回滚流程
- MQ 堆积处理规范

MVP 可以先使用关键词检索，同时保持服务接口不变：

- `KeywordKnowledgeSearchService`
- `VectorKnowledgeSearchService`

### 5.6 证据链模块

每个工具结果都需要转换成结构化证据。

示例：

```json
{
  "sourceType": "LOG",
  "sourceName": "order-service-error.log",
  "content": "14:03 后发现 182 次 NullPointerException。",
  "confidence": 0.87,
  "metadata": {
    "service": "order-service",
    "timeRange": "14:00-14:30"
  }
}
```

最终答案必须基于 Evidence 生成。这个设计可以减少幻觉，也方便面试时解释 Agent 为什么得出某个结论。

### 5.7 审计与可观测性

OpsPilot 需要记录完整执行过程：

- 用户问题
- 生成的执行计划
- 每个 Step 的输入和输出
- 每次 Tool 的请求和响应
- 每个步骤的耗时
- 失败原因
- 最终答案
- 证据列表

这样可以追踪 Agent 的决策过程，也方便定位工具调用或模型输出中的问题。

### 5.8 SSE 实时推送

Agent 执行过程中通过 SSE 推送事件。

事件示例：

```json
{
  "taskId": "task-001",
  "eventType": "STEP_STARTED",
  "message": "开始查询 order-service 错误日志。",
  "timestamp": "2026-04-30T10:00:00"
}
```

事件类型：

```text
TASK_CREATED
INTENT_RECOGNIZED
PLAN_GENERATED
STEP_STARTED
TOOL_RESULT_RECEIVED
EVIDENCE_COLLECTED
ANSWER_GENERATED
TASK_FAILED
```

## 6. 本地 Demo 数据

项目使用本地仿真数据进行可复现演示，避免使用公司真实生产数据。

推荐目录结构：

```text
demo-data
├── logs
│   ├── order-service-error.log
│   ├── payment-service-slow.log
│   └── inventory-service-redis-timeout.log
│
├── metrics
│   ├── order-service-metrics.json
│   ├── payment-service-metrics.json
│   └── inventory-service-metrics.json
│
├── docs
│   ├── interface-500-troubleshooting.md
│   ├── slow-sql-guide.md
│   ├── redis-timeout-sop.md
│   └── rollback-guide.md
│
├── tickets
│   ├── ticket-1001.json
│   └── ticket-1002.json
│
└── git
    ├── order-service-commits.json
    └── payment-service-commits.json
```

内置演示问题：

```text
1. order-service 今天 14:00 后大量 500，帮我排查。
2. payment-service 最近 30 分钟变慢了，看下是不是数据库问题。
3. inventory-service Redis 超时比较多，帮我分析原因。
```

## 7. 数据库设计

### 7.1 `agent_task`

```text
id
user_query
intent
status
final_answer
error_message
created_at
updated_at
```

### 7.2 `agent_step`

```text
id
task_id
step_name
tool_name
status
params_json
result_json
error_message
started_at
finished_at
```

### 7.3 `tool_audit_log`

```text
id
task_id
step_id
tool_name
request_json
response_json
success
duration_ms
error_message
created_at
```

### 7.4 `evidence`

```text
id
task_id
step_id
source_type
source_name
content
confidence
metadata_json
created_at
```

### 7.5 `knowledge_document`

```text
id
title
source_type
content
created_at
```

### 7.6 `knowledge_chunk`

```text
id
document_id
chunk_text
embedding
metadata_json
created_at
```

### 7.7 `agent_event`

```text
id
task_id
event_type
message
payload_json
created_at
```

### 7.8 `approval_record`

这个表用于后续版本。

```text
id
task_id
step_id
action_name
risk_level
status
requested_payload
approved_by
created_at
updated_at
```

## 8. 示例执行流程

用户输入：

```text
order-service 今天 14:00 后大量 500，帮我排查。
```

执行过程：

```text
1. 创建 AgentTask，状态为 CREATED。
2. 识别意图为 INCIDENT_5XX_DIAGNOSIS。
3. 生成执行计划：
   - LogSearchTool
   - MetricQueryTool
   - GitChangeTool
   - KnowledgeSearchTool
   - AnswerComposer
4. 查询错误日志。
5. 查询错误率监控。
6. 查询 14:00 附近的 Git 变更。
7. 检索 HTTP 500 排查手册。
8. 将工具结果转换成证据。
9. 生成最终排障报告。
10. 返回根因推测、证据链、处理建议和风险提示。
```

示例证据：

```text
- 14:03 后出现 182 次 NullPointerException。
- HTTP 5xx rate 从 0.2% 上升到 18.7%。
- 13:52 有一次发布，修改了请求参数校验逻辑。
- HTTP 500 排查手册建议优先检查最近发布变更。
```

示例结论：

```text
本次故障疑似由 13:52 发布引入的空值兼容问题导致。建议优先回滚可疑 commit，
补充空值校验，并对订单创建接口进行回归测试。
```

## 9. LLM 使用边界

OpsPilot 不会把所有控制权都交给 LLM。

LLM 适合负责：

- 意图识别
- 后续版本中的计划生成
- 工具结果摘要
- 最终报告生成
- 基于知识库的回答生成

后端系统负责：

- 任务状态推进
- 工具执行
- 权限判断
- 参数校验
- 审计记录
- 异常处理
- SSE 事件推送
- 证据持久化

这个边界能让 Agent 更可控，也更容易调试。

## 10. 版本规划

### 10.1 MVP 版本

- Spring Boot 项目骨架
- 规则 Planner
- 本地日志查询工具
- 本地监控查询工具
- 本地关键词知识库检索
- Agent 任务状态机
- 任务、步骤、审计和证据持久化
- 基础排障报告生成
- Swagger 演示

### 10.2 增强版本

- 接入 LangChain4j
- SSE 执行事件实时推送
- 基于 pgvector 的 RAG
- 本地 Git 变更工具
- 本地工单工具
- Docker Compose

### 10.3 简历版本

- 完整 RAG 文档入库和检索流程
- 工具风险等级
- 人工确认机制
- 基于 Redis 的事件或任务缓存
- Elasticsearch、Prometheus、GitLab 适配器设计
- 单元测试和演示脚本

## 11. 简历亮点

- 基于状态机实现 Agent 任务编排，而不是一次性 LLM 调用。
- 设计 Tool Adapter 层，统一封装日志、监控、Git、工单和知识库检索能力。
- 构建本地仿真数据集，支持可复现的端到端排障演示。
- 使用 RAG 检索故障手册，生成基于证据的排障报告。
- 记录步骤级审计日志和证据链，提升 Agent 行为可追溯性。
- 设计高风险操作的人工确认机制，避免 Agent 自主执行危险动作。

## 12. 推荐开发顺序

1. 定义任务、步骤、工具和证据的领域模型。
2. 实现 `AgentTask` 和 `AgentStep` 持久化。
3. 实现 `OpsTool` 接口和本地 demo 工具。
4. 实现规则 Planner。
5. 实现 `AgentOrchestrator`。
6. 实现 `EvidenceCollector` 和 `AnswerComposer`。
7. 接入 LLM 生成最终报告。
8. 增加 SSE 执行事件推送。
9. 增加基于 pgvector 的 RAG。
10. 增加 Git、工单和人工确认能力。

