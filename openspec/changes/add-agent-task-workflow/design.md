## Context

OpsPilot 已经完成项目基础规格，正式目标是 Multi-Agent 智能运维排障系统。按照迭代路线，V1 先实现单 Orchestrator + 多 Tool 的排障闭环。

本次 change 是 V1 的第一部分：建立 Agent 任务工作流。它不负责真实工具调用，也不负责 LLM 报告生成，而是先建立任务、步骤、状态机和基础 API。后续 Tool Adapter、证据链、SSE 和 Multi-Agent 都会复用这套模型。

## Goals / Non-Goals

**Goals:**

- 定义 AgentTask 和 AgentStep 的领域模型。
- 定义任务状态和步骤状态。
- 定义任务创建和查询 API。
- 定义任务状态机的合法流转。
- 为后续 Orchestrator、ToolExecutionService 和 SupervisorAgent 预留扩展点。

**Non-Goals:**

- 不实现真实 LLM 调用。
- 不实现工具调用。
- 不实现 RAG。
- 不实现 SSE。
- 不实现 Multi-Agent 调度。

## Decisions

### Decision 1: 任务和步骤分离

一次用户请求对应一个 `AgentTask`，任务下包含多个 `AgentStep`。任务记录用户问题、意图、总状态和最终答案；步骤记录具体执行阶段、工具名、参数、结果和步骤状态。

这样可以支持步骤级审计、失败定位、后续重试和 SSE 事件推送。

### Decision 2: V1 先使用同步工作流

任务创建接口可以先同步创建任务、生成初始步骤并返回任务详情。后续再通过异步执行和 SSE 推送增强。

第一版避免过早引入消息队列或复杂线程模型，把重点放在状态和数据模型稳定性上。

### Decision 3: 状态机由后端显式控制

LLM 不直接决定任务状态。状态推进由后端 `TaskStateMachine` 控制，所有非法状态流转必须拒绝。

这能避免 Agent 执行过程不可控，也便于在面试中解释系统可靠性设计。

### Decision 4: 领域模型为 Multi-Agent 预留上下文字段

虽然 V1 不实现 Multi-Agent，但任务模型需要保留 `intent`、`metadata`、`context` 等扩展字段。后续 `SupervisorAgent` 可以基于同一任务模型调度专业 Agent。

### Decision 5: 持久化优先使用 MySQL，MVP 可用 H2 替代

目标主数据库是 MySQL。为了让早期开发更轻，MVP 可以使用 H2 或本地 MySQL，Repository 层保持一致。RAG 阶段如需向量检索，再单独引入 pgvector、Milvus 或其他向量库。

## Risks / Trade-offs

- [Risk] 同步任务创建无法展示完整 Agent 执行过程。→ 后续 `agent-event-stream` change 引入 SSE 和异步执行。
- [Risk] 状态过多导致第一版实现复杂。→ V1 只实现核心状态，`WAITING_APPROVAL` 放到风险控制阶段。
- [Risk] 过早设计 Multi-Agent 字段造成模型臃肿。→ 只保留必要扩展字段，不实现实际协作逻辑。
