## ADDED Requirements

### Requirement: V0 项目基础
项目 SHALL 在 V0 阶段建立 README、技术方案、OpenSpec 规格、迭代计划和 Spring Boot 项目骨架。

#### Scenario: V0 可验收
- **WHEN** V0 完成
- **THEN** 仓库 MUST 包含清晰项目文档、OpenSpec 基础规格，并能启动基础 Spring Boot 应用

### Requirement: V1 单 Agent 排障闭环
项目 SHALL 在 V1 阶段实现用户问题到计划生成、工具调用、证据汇总和报告生成的完整链路。

#### Scenario: V1 可完成 500 排障演示
- **WHEN** 用户提交 order-service 500 排障问题
- **THEN** 系统 MUST 生成任务步骤、调用本地工具、收集证据并输出排障报告

### Requirement: V2 RAG 和可观测性
项目 SHALL 在 V2 阶段实现知识库检索、工具调用审计、Agent 事件记录和 SSE 执行流。

#### Scenario: V2 报告包含知识依据
- **WHEN** 系统生成排障报告
- **THEN** 报告 MUST 包含来自故障手册或 SOP 的依据，并且工具调用过程可追踪

### Requirement: V3 Multi-Agent 初版
项目 SHALL 在 V3 阶段引入 Supervisor Agent 和多个 Specialist Agent，并统一 AgentContext 与 AgentResult。

#### Scenario: V3 可调度多个专业 Agent
- **WHEN** Supervisor Agent 接收排障任务
- **THEN** 它 MUST 能调度日志、监控、变更和知识库等专业 Agent，并汇总标准化结果

### Requirement: V4 Multi-Agent 协作增强
项目 SHALL 在 V4 阶段支持专业 Agent 并行执行、结论冲突标记、风险审查和人工确认。

#### Scenario: V4 可处理冲突和风险
- **WHEN** 多个 Agent 给出不一致结论或高风险建议
- **THEN** 系统 MUST 标记不确定性或进入人工确认流程

### Requirement: V5 演示与简历增强
项目 SHALL 在 V5 阶段完善 Docker Compose、演示脚本、测试、README 演示流程、架构图和简历描述。

#### Scenario: V5 可在新环境演示
- **WHEN** 开发者在新机器拉取项目
- **THEN** 系统 MUST 提供清晰启动步骤和可复现演示流程

