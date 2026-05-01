## 1. 项目骨架

- [ ] 1.1 创建 Spring Boot 3.x + Java 17 + Maven 项目骨架
- [ ] 1.2 添加 Web、Validation、Persistence、Test 基础依赖
- [ ] 1.3 配置应用名称、基础 profile 和本地启动配置

## 2. 领域模型

- [ ] 2.1 定义 `AgentTask` 领域模型
- [ ] 2.2 定义 `AgentStep` 领域模型
- [ ] 2.3 定义 `AgentTaskStatus` 枚举
- [ ] 2.4 定义 `AgentStepStatus` 枚举
- [ ] 2.5 定义创建任务和查询任务 DTO

## 3. 持久化

- [ ] 3.1 创建 `agent_task` 表结构
- [ ] 3.2 创建 `agent_step` 表结构
- [ ] 3.3 实现任务 Repository
- [ ] 3.4 实现步骤 Repository

## 4. 状态机

- [ ] 4.1 实现 `TaskStateMachine`
- [ ] 4.2 定义合法任务状态流转规则
- [ ] 4.3 为非法状态流转添加异常处理
- [ ] 4.4 编写状态机单元测试

## 5. 任务 API

- [ ] 5.1 实现 `POST /api/agent/tasks`
- [ ] 5.2 实现 `GET /api/agent/tasks/{taskId}`
- [ ] 5.3 实现请求参数校验和错误响应
- [ ] 5.4 添加 Swagger / Knife4j 接口说明

## 6. 规则计划生成

- [ ] 6.1 实现 V1 规则 Planner
- [ ] 6.2 为 500 排障问题生成初始 AgentStep 列表
- [ ] 6.3 为未知问题类型返回可解释的暂不支持响应

## 7. 验证

- [ ] 7.1 运行 OpenSpec validate，确保 change 合法
- [ ] 7.2 运行单元测试
- [ ] 7.3 通过接口创建并查询一个 AgentTask
- [ ] 7.4 提交 `add-agent-task-workflow` change 和实现代码

