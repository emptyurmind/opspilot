# OpsPilot Technical Design

## 1. Project Positioning

### 1.1 Name

OpsPilot: an intelligent incident diagnosis agent for microservice engineering teams.

### 1.2 Background

In microservice systems, online incident diagnosis usually requires engineers to check
application logs, metrics, release records, Git changes, runbooks, and ticket history.
The information is scattered across multiple systems, and diagnosis heavily depends on
individual experience.

OpsPilot uses agent orchestration, tool calling, and RAG-based knowledge retrieval to
aggregate multi-source evidence and generate traceable diagnosis conclusions and action
suggestions.

### 1.3 Goals

- Understand natural language diagnosis requests.
- Generate diagnosis plans automatically.
- Invoke tools for logs, metrics, Git changes, knowledge documents, and tickets.
- Collect and normalize evidence.
- Generate traceable diagnosis reports.
- Record task steps, tool calls, and execution audit logs.
- Support local demo data for reproducible demonstrations.
- Provide human approval for high-risk actions in later versions.

### 1.4 Non-goals

- Build a universal AIOps platform.
- Use real company production data.
- Let the LLM directly execute high-risk operations.
- Implement model training, fine-tuning, or complex multi-agent research features.

## 2. Technical Stack

### 2.1 MVP Stack

- Java 17
- Spring Boot 3.x
- Maven
- H2 or PostgreSQL
- Local demo data
- Rule-based planner
- Keyword-based knowledge retrieval
- Swagger or Knife4j
- JUnit 5

### 2.2 Target Stack

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

### 2.3 Optional Integrations

- Elasticsearch for log search
- Prometheus for metric query
- GitLab or GitHub for change analysis
- Jira or local ticket system for ticket analysis
- Kafka or RabbitMQ for asynchronous execution

## 3. High-level Architecture

```text
User / Frontend / Swagger
        |
        v
API Layer
        |
        v
Agent Orchestration Layer
        |
        +-------------------+
        |                   |
        v                   v
Tool Calling Layer     RAG Knowledge Layer
        |                   |
        v                   v
Local Demo Data        PostgreSQL + pgvector
Logs / Metrics / Git   Documents / Chunks / Embeddings
        |
        v
Evidence Collection + Answer Composition
        |
        v
SSE Events / Task Result Query
```

Detailed runtime structure:

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
demo logs           demo metrics        docs / pgvector
```

## 4. Module Layers

Recommended package structure:

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

## 5. Core Modules

### 5.1 API Layer

The API layer exposes task creation, task query, execution event streaming, and knowledge
management endpoints.

Planned APIs:

```http
POST /api/agent/tasks
GET /api/agent/tasks/{taskId}
GET /api/agent/tasks/{taskId}/events
POST /api/knowledge/documents
POST /api/tickets/{ticketId}/analyze
```

MVP APIs:

```http
POST /api/agent/tasks
GET /api/agent/tasks/{taskId}
GET /api/agent/tasks/{taskId}/events
```

### 5.2 Agent Orchestration Layer

The orchestration layer is the core of the project. It turns one user request into a
stateful agent task.

Execution flow:

```text
Receive user query
-> Create agent_task
-> Recognize intent
-> Generate plan
-> Execute tools
-> Collect evidence
-> Compose final answer
-> Update task status
-> Publish execution events
```

Task statuses:

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

Step statuses:

```text
PENDING
RUNNING
SUCCESS
FAILED
SKIPPED
WAITING_APPROVAL
```

### 5.3 Planning Service

The MVP uses a rule-based planner to keep the first version deterministic and testable.

Example rules:

```text
Query contains "500", "error", or "exception"
-> INCIDENT_5XX_DIAGNOSIS

Query contains "slow", "timeout", "RT", or "latency"
-> PERFORMANCE_DIAGNOSIS

Query contains "ticket" or "工单"
-> TICKET_ANALYSIS
```

Example plan for `INCIDENT_5XX_DIAGNOSIS`:

```text
1. Search error logs.
2. Query error-rate metrics.
3. Query Git changes around the incident time.
4. Search incident runbooks.
5. Compose diagnosis report.
```

A later version can add `LlmPlanningService`, where the model generates a JSON execution
plan and the backend validates tool names, parameters, and risk levels.

### 5.4 Tool Calling Layer

Tools are exposed through a unified adapter interface.

```java
public interface OpsTool {
    ToolDefinition definition();
    ToolResult execute(ToolRequest request);
}
```

Each tool should define:

- Tool name
- Tool description
- Parameter schema
- Risk level
- Timeout
- Whether human approval is required
- Execution logic

MVP tools:

- `LocalLogSearchTool`
- `LocalMetricQueryTool`
- `LocalKnowledgeSearchTool`
- `LocalGitChangeTool`

Future adapters:

- `ElasticsearchLogSearchTool`
- `PrometheusMetricQueryTool`
- `GitLabChangeTool`
- `JiraTicketTool`

The orchestration layer depends only on `OpsTool`, so local demo data can be replaced by
real external systems without changing the agent workflow.

### 5.5 RAG Knowledge Layer

The knowledge layer retrieves runbooks and technical documents for diagnosis.

Target flow:

```text
Upload Markdown document
-> Clean text
-> Split into chunks
-> Generate embeddings
-> Store chunks in pgvector
-> Embed user query
-> Retrieve top-k chunks
-> Inject retrieved context into the final answer prompt
```

Knowledge document examples:

- Redis timeout troubleshooting SOP
- Slow SQL optimization guide
- HTTP 500 troubleshooting guide
- Release rollback guide
- MQ backlog handling guide

The MVP can use keyword-based retrieval first while keeping the same service interface:

- `KeywordKnowledgeSearchService`
- `VectorKnowledgeSearchService`

### 5.6 Evidence Collection

Every tool result should be converted into structured evidence.

Example evidence:

```json
{
  "sourceType": "LOG",
  "sourceName": "order-service-error.log",
  "content": "182 NullPointerException errors were found after 14:03.",
  "confidence": 0.87,
  "metadata": {
    "service": "order-service",
    "timeRange": "14:00-14:30"
  }
}
```

The final answer must be grounded in evidence. This reduces hallucination and makes the
agent result easier to verify.

### 5.7 Audit and Observability

OpsPilot records the full execution process:

- User query
- Generated plan
- Step input and output
- Tool request and response
- Duration of each step
- Error message
- Final answer
- Evidence list

This design allows engineers to inspect why the agent generated a certain conclusion.

### 5.8 Server-Sent Events

The agent publishes execution events through SSE.

Example event:

```json
{
  "taskId": "task-001",
  "eventType": "STEP_STARTED",
  "message": "Searching order-service error logs.",
  "timestamp": "2026-04-30T10:00:00"
}
```

Event types:

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

## 6. Local Demo Data

The project uses local simulation data to provide reproducible demonstrations without
using company production data.

Recommended structure:

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

Built-in demo queries:

```text
1. order-service had many 500 errors after 14:00. Please diagnose it.
2. payment-service has been slow in the last 30 minutes. Check whether it is a database issue.
3. inventory-service has many Redis timeout errors. Analyze the possible root cause.
```

## 7. Database Design

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

This table is planned for later versions.

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

## 8. Example Execution Flow

User query:

```text
order-service had many 500 errors after 14:00. Please diagnose it.
```

Execution:

```text
1. Create AgentTask with status CREATED.
2. Recognize intent as INCIDENT_5XX_DIAGNOSIS.
3. Generate plan:
   - LogSearchTool
   - MetricQueryTool
   - GitChangeTool
   - KnowledgeSearchTool
   - AnswerComposer
4. Search error logs.
5. Query error-rate metrics.
6. Query Git changes around 14:00.
7. Retrieve 500 troubleshooting runbooks.
8. Convert results into evidence.
9. Generate final diagnosis report.
10. Return root cause hypothesis, evidence, suggestions, and risk notes.
```

Example evidence:

```text
- NullPointerException appeared 182 times after 14:03.
- HTTP 5xx rate increased from 0.2% to 18.7%.
- A release happened at 13:52 and changed request validation logic.
- The HTTP 500 runbook suggests checking recent releases first.
```

Example conclusion:

```text
The incident is likely caused by a null-value compatibility issue introduced in the
13:52 release. Suggested actions are to roll back the suspicious commit, add null checks,
and run regression tests for the order creation API.
```

## 9. LLM Usage Boundaries

OpsPilot does not hand over all control to the LLM.

The LLM can be used for:

- Intent recognition
- Plan generation in later versions
- Tool result summarization
- Final report generation
- Knowledge-based answering

The backend owns:

- Task state transitions
- Tool execution
- Permission checks
- Parameter validation
- Audit logging
- Exception handling
- SSE event publishing
- Evidence persistence

This boundary makes the agent more predictable and easier to debug.

## 10. Version Plan

### 10.1 MVP

- Spring Boot project skeleton
- Rule-based planner
- Local log search tool
- Local metric query tool
- Local keyword knowledge search
- Agent task state machine
- Task, step, audit, and evidence persistence
- Basic final answer generation
- Swagger demonstration

### 10.2 Enhanced Version

- LangChain4j integration
- SSE execution event streaming
- pgvector-based RAG
- Local Git change tool
- Local ticket tool
- Docker Compose

### 10.3 Resume-ready Version

- Complete RAG ingestion and retrieval flow
- Tool risk levels
- Human approval design
- Redis-based event or task cache
- Elasticsearch, Prometheus, and GitLab adapter design
- Unit tests and demo scripts

## 11. Resume Highlights

- Implemented state-machine-based agent task orchestration instead of one-shot LLM calls.
- Designed a tool adapter layer for logs, metrics, Git changes, tickets, and knowledge retrieval.
- Built local simulation data for reproducible incident diagnosis demos.
- Used RAG to retrieve runbooks and generate evidence-grounded diagnosis reports.
- Recorded step-level audit logs and evidence chains for traceable agent behavior.
- Designed human approval for high-risk actions to avoid unsafe autonomous operations.

## 12. Development Order

1. Define domain models for task, step, tool, and evidence.
2. Implement persistence for `AgentTask` and `AgentStep`.
3. Implement the `OpsTool` interface and local demo tools.
4. Implement the rule-based planner.
5. Implement `AgentOrchestrator`.
6. Implement `EvidenceCollector` and `AnswerComposer`.
7. Integrate an LLM for final report generation.
8. Add SSE event streaming.
9. Add pgvector-based RAG.
10. Add Git, ticket, and approval capabilities.

