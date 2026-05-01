# OpsPilot

OpsPilot 是一个面向微服务研发团队的 Multi-Agent 智能运维排障系统。

这个项目会以 Java 后端系统的方式实现，用来展示 Multi-Agent 协同、Agent 编排、工具调用、RAG 知识检索、证据链追踪和执行审计等能力。它不是一个简单的聊天机器人，而是一个可本地演示、可逐步接入真实日志/监控/Git/工单系统的后端 Agent 项目。

## 项目目标

在微服务系统中，线上问题排查通常需要研发人员在日志平台、监控平台、发布记录、Git 变更、故障手册和工单系统之间反复切换。OpsPilot 希望通过 Supervisor Agent 调度多个专业 Agent，自动聚合多源证据，并生成可追溯的排障结论和处理建议。

## 核心能力

- 根据自然语言问题识别排障意图
- 自动生成排障步骤
- 通过 Supervisor Agent 调度日志、监控、变更、知识库等专业 Agent
- 调用日志、监控、Git、知识库、工单等工具
- 汇总结构化证据链
- 生成最终排障报告
- 记录任务步骤、工具调用和执行审计
- 通过本地仿真数据支持可复现演示

## 开发方式

项目采用轻量级 SDD / OpenSpec 工作流管理功能演进。核心能力会先通过 OpenSpec 定义 proposal、design、spec 和 tasks，再进入代码实现。

当前 OpenSpec 入口：

- `openspec/changes/establish-project-foundation/`
- `openspec/specs/`，归档后生成正式 capability specs

## 技术方案

- [技术方案设计](docs/technical-design.md)

## 计划技术栈

- Java 17
- Spring Boot 3.x
- LangChain4j
- PostgreSQL + pgvector
- Redis
- Server-Sent Events
- Docker Compose
