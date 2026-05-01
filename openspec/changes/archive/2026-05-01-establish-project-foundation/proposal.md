## Why

OpsPilot 需要先明确项目目标、技术栈边界、迭代路线和 SDD 工作方式，避免 Agent 项目在实现过程中演变成边界不清的 Prompt 或工具调用堆叠。

本次变更用于建立项目基础规格，让后续单 Agent 闭环、RAG、SSE 和 Multi-Agent 演进都能按 OpenSpec 管理需求、设计、任务和验收标准。

## What Changes

- 明确 OpsPilot 的项目定位：面向微服务研发团队的 Multi-Agent 智能运维排障系统。
- 明确第一阶段采用单 Orchestrator + 多 Tool 的实现路径，后续演进为 Supervisor Agent + 多 Specialist Agent。
- 明确目标技术栈：Java 17、Spring Boot 3.x、LangChain4j、PostgreSQL、pgvector、Redis、SSE、Docker Compose。
- 明确本地仿真数据优先的演示方式，真实外部系统先通过 Adapter 设计保留扩展点。
- 建立 OpenSpec/SDD 工作流，用 proposal、design、spec 和 tasks 管理项目演进。
- 新增项目基础规格、迭代计划规格和 Multi-Agent 演进规格。

## Capabilities

### New Capabilities

- `project-foundation`: 定义项目目标、范围、核心场景、技术栈边界和 SDD 工作方式。
- `iteration-roadmap`: 定义从项目基础到单 Agent 闭环、RAG、SSE、Multi-Agent 和演示完善的迭代计划。
- `multi-agent-evolution`: 定义从单 Orchestrator 演进为 Supervisor + Specialist Agents 的目标架构和角色边界。

### Modified Capabilities

无。当前仓库没有已存在的 OpenSpec capability。

## Impact

- 新增 `openspec/` 下的项目规格和变更文档。
- 更新项目文档，使 README 和技术方案与 Multi-Agent 与 SDD 目标保持一致。
- 暂不修改业务代码，不引入运行时依赖。

