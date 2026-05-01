## ADDED Requirements

### Requirement: 创建 Agent 任务
系统 SHALL 提供 API 创建 Agent 排障任务，并持久化用户问题、任务状态和创建时间。

#### Scenario: 成功创建任务
- **WHEN** 用户提交有效排障问题到 `POST /api/agent/tasks`
- **THEN** 系统 MUST 创建一个状态为 `CREATED` 的 AgentTask 并返回任务 ID

#### Scenario: 拒绝空问题
- **WHEN** 用户提交空字符串或只包含空白字符的问题
- **THEN** 系统 MUST 返回参数校验错误，且不得创建 AgentTask

### Requirement: 查询 Agent 任务
系统 SHALL 提供 API 查询 Agent 任务详情，包括任务基础信息、当前状态、步骤列表和最终答案。

#### Scenario: 查询存在的任务
- **WHEN** 用户请求 `GET /api/agent/tasks/{taskId}` 且任务存在
- **THEN** 系统 MUST 返回任务详情和关联步骤列表

#### Scenario: 查询不存在的任务
- **WHEN** 用户请求不存在的 taskId
- **THEN** 系统 MUST 返回任务不存在错误

### Requirement: 任务状态机
系统 SHALL 使用显式状态机控制 AgentTask 状态流转，非法流转必须被拒绝。

#### Scenario: 合法状态流转
- **WHEN** 任务从 `CREATED` 进入意图识别阶段
- **THEN** 系统 MUST 允许状态流转到 `INTENT_RECOGNIZED`

#### Scenario: 非法状态流转
- **WHEN** 任务从 `CREATED` 直接流转到 `ANSWER_GENERATED`
- **THEN** 系统 MUST 拒绝该状态流转

### Requirement: Agent 步骤管理
系统 SHALL 支持为一个 AgentTask 创建多个 AgentStep，并记录步骤名称、步骤状态、工具名称、参数和结果。

#### Scenario: 创建任务步骤
- **WHEN** 系统生成排障计划
- **THEN** 系统 MUST 为任务创建状态为 `PENDING` 的步骤列表

#### Scenario: 步骤执行完成
- **WHEN** 某个步骤执行成功
- **THEN** 系统 MUST 将步骤状态更新为 `SUCCESS` 并记录执行结果

### Requirement: V1 初始执行计划
系统 SHALL 在 V1 使用规则方式为 500 排障场景生成初始步骤，不依赖 LLM Planner。

#### Scenario: 500 排障计划生成
- **WHEN** 用户问题包含 `500`、`报错` 或 `异常`
- **THEN** 系统 MUST 生成日志查询、监控查询、变更查询、知识库检索和报告生成步骤
