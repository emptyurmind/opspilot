## Context

项目初始方案使用 PostgreSQL + pgvector 作为目标技术栈。经过技术取舍，主业务库改为 MySQL，以更符合 Java 后端简历项目和国内后端面试语境。

向量检索仍然是后续 RAG 阶段能力，但不再要求与主业务库绑定。

## Goals / Non-Goals

**Goals:**

- 主业务库采用 MySQL。
- 本地开发继续使用 H2，降低启动成本。
- SQL schema 尽量保持 MySQL/H2 兼容。
- RAG 阶段的向量库选择保持开放。

**Non-Goals:**

- 本次不实现 RAG 向量检索。
- 本次不引入 Docker Compose MySQL。
- 本次不迁移已有生产数据。

## Decisions

### Decision 1: MySQL 作为主业务库

AgentTask、AgentStep、ToolAudit、Evidence 等业务表优先存储在 MySQL。MySQL 对 Java 后端面试和业务系统经验表达更友好。

### Decision 2: H2 作为本地开发库

`local` profile 继续使用 H2，并开启 MySQL compatibility mode。这样可以在没有 MySQL 的环境里快速启动和测试基础功能。

### Decision 3: 向量检索后续单独选型

RAG 阶段不再默认绑定 pgvector。后续可根据实际实现选择 pgvector、Milvus、Elasticsearch Vector 或其他向量库。

## Risks / Trade-offs

- [Risk] H2 MySQL mode 不能覆盖所有 MySQL 行为。→ 第一阶段只使用基础 SQL 类型和约束，MySQL profile 后续用 Docker Compose 验证。
- [Risk] 移除 pgvector 会让 RAG 技术栈暂时不完整。→ RAG 阶段单独建 OpenSpec change 完成向量库选型。

