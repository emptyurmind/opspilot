## Context

OpsPilot 当前只有 README 和技术方案文档，还没有可执行代码和 OpenSpec 规格。项目目标已经从单一智能运维 Agent 调整为面向微服务研发团队的 Multi-Agent 智能运维排障系统。

这个项目同时承担两个目标：一是做出一个可本地演示的后端 Agent 项目，二是在简历和面试中展示对 SDD/OpenSpec、Agent 编排、RAG、工具调用和 Multi-Agent 演进的理解。因此需要先固化架构边界和迭代路线，再进入代码实现。

## Goals / Non-Goals

**Goals:**

- 建立 OpenSpec 规格驱动开发流程，后续功能必须先有 change 和验收标准，再进入实现。
- 将项目定位明确为 Multi-Agent 智能运维排障系统，但第一阶段实现单 Orchestrator + 多 Tool 的最小闭环。
- 明确目标技术栈和暂缓引入的技术，控制第一阶段复杂度。
- 明确迭代计划，让项目从基础规格、单 Agent 闭环、RAG/SSE，再演进到 Multi-Agent。
- 明确本地仿真数据优先，避免依赖真实公司系统或生产数据。

**Non-Goals:**

- 本次变更不创建 Spring Boot 业务代码。
- 本次变更不接入 LLM、数据库、Redis 或 pgvector。
- 本次变更不实现真实日志平台、监控平台、GitLab 或工单系统集成。
- 本次变更不实现完整 Multi-Agent 协作，只定义演进目标和角色边界。

## Decisions

### Decision 1: 使用 OpenSpec 管理 SDD 流程

选择 OpenSpec 作为规格驱动开发工具，原因是它能把 proposal、design、spec 和 tasks 放在仓库内，与代码一起版本化。相比只在聊天记录中维护计划，OpenSpec 更适合 AI 辅助开发中的上下文恢复、变更追踪和验收标准管理。

备选方案是手写 `docs/specs` 目录。该方式更轻量，但缺少 OpenSpec CLI 的 validate、archive 和 change 管理能力。

### Decision 2: 项目目标定为 Multi-Agent，但实现从单 Orchestrator 起步

项目最终定位为 Multi-Agent 智能运维排障系统，但 V1 不直接实现多个 Agent。第一阶段先实现单 Orchestrator + 多 Tool 的完整排障闭环，确保任务状态、工具调用、证据链和报告生成模型稳定。

直接从 Multi-Agent 开始会导致角色边界、上下文传递、并行执行、冲突处理和失败降级同时出现，复杂度过高。单 Orchestrator 起步能保留演进路径，同时更快获得可运行成果。

### Decision 3: 技术栈以 Java 后端工程为核心

目标技术栈选择 Java 17、Spring Boot 3.x、LangChain4j、PostgreSQL、pgvector、Redis、SSE 和 Docker Compose。这个组合既贴近 Java 后端岗位，也能覆盖 Agent 项目的核心工程能力。

Python LangChain/LangGraph 更适合快速 AI 实验，但本项目需要体现 Java 后端系统设计，因此不作为主实现栈。LangGraph 的状态编排思想会被借鉴，但不直接引入 Python 技术栈。

### Decision 4: 本地仿真数据优先，真实系统通过 Adapter 演进

V1 使用 `demo-data` 模拟日志、监控、Git、工单和故障文档。工具层通过 Adapter 设计保留真实外部系统替换能力。

这样可以避免个人项目依赖真实公司数据，同时保证演示可复现。后续可以将 `LocalLogSearchTool` 替换为 `ElasticsearchLogSearchTool`，将 `LocalMetricQueryTool` 替换为 `PrometheusMetricQueryTool`。

### Decision 5: 前端暂缓，优先用 Swagger、curl 和 SSE 演示

第一阶段不实现 React/Vue 前端，避免分散后端和 Agent 编排重点。系统先通过 Swagger、curl 和 SSE 接口展示任务创建、执行过程和最终报告。

后续如果需要展示效果，可以增加轻量前端或静态演示页面。

## Risks / Trade-offs

- [Risk] OpenSpec 文档过多导致开发节奏变慢。→ 每个 change 控制在清晰、可实现的范围内，优先围绕验收标准写必要规格。
- [Risk] 项目名义上是 Multi-Agent，但早期实现还是单 Orchestrator。→ 在规格和 README 中明确“分阶段演进”，避免过度承诺。
- [Risk] 本地仿真数据被认为不够真实。→ 设计完整证据链和 Adapter 接口，说明真实接入时只替换工具适配器。
- [Risk] 技术栈一次性引入过多。→ V1 只保留 Spring Boot、持久化和本地工具，pgvector、Redis、SSE 和 LangChain4j 按迭代引入。
- [Risk] LLM 输出不稳定影响演示。→ V1 使用规则 Planner 和可控本地数据，LLM 仅用于最终报告生成或后续增强。

