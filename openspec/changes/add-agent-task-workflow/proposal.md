## Why

OpsPilot 的 V1 需要先跑通“用户问题 -> 任务创建 -> 状态流转 -> 步骤执行 -> 结果查询”的最小闭环。没有稳定的任务工作流，后续 Tool Adapter、证据链、RAG、SSE 和 Multi-Agent 都缺少承载基础。

本次变更建立 AgentTask、AgentStep、任务状态机和基础 API 规格，作为后续排障链路实现的地基。

## What Changes

- 新增 Agent 任务工作流能力，定义任务、步骤、状态和事件的基础行为。
- 定义 `POST /api/agent/tasks` 创建排障任务。
- 定义 `GET /api/agent/tasks/{taskId}` 查询任务详情。
- 定义任务状态流转：`CREATED`、`INTENT_RECOGNIZED`、`PLAN_GENERATED`、`TOOL_EXECUTING`、`EVIDENCE_COLLECTED`、`ANSWER_GENERATED`、`FAILED`。
- 定义步骤状态流转：`PENDING`、`RUNNING`、`SUCCESS`、`FAILED`、`SKIPPED`。
- V1 暂不实现完整 Multi-Agent，只保留后续 Supervisor Agent 可复用的任务模型和执行上下文。

## Capabilities

### New Capabilities

- `agent-task-workflow`: 定义 Agent 任务、步骤、状态机、基础 API 和任务查询行为。

### Modified Capabilities

- `iteration-roadmap`: 将 V1 的“单 Agent 排障闭环”细化为先实现 Agent 任务工作流，再实现 Tool Adapter 和证据链。

## Impact

- 后续将新增 Spring Boot 项目骨架、任务领域模型、Controller、Application Service、Repository 和状态机组件。
- 后续将新增任务和步骤持久化表。
- 本次 OpenSpec change 不直接接入 LLM、RAG、SSE、Redis 或真实外部系统。

