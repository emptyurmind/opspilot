## MODIFIED Requirements

### Requirement: V1 单 Agent 排障闭环
项目 SHALL 在 V1 阶段实现用户问题到计划生成、工具调用、证据汇总和报告生成的完整链路，并优先建立 Agent 任务工作流作为后续 Tool Adapter 和证据链的基础。

#### Scenario: V1 可完成 500 排障演示
- **WHEN** 用户提交 order-service 500 排障问题
- **THEN** 系统 MUST 生成任务步骤、调用本地工具、收集证据并输出排障报告

#### Scenario: V1 先完成任务工作流
- **WHEN** V1 的第一阶段完成
- **THEN** 系统 MUST 支持任务创建、任务查询、步骤持久化和任务状态机

