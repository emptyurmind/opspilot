## Why

OpsPilot 的主数据库决定改为 MySQL，以更贴近 Java 后端岗位和运维排障中的慢 SQL、索引、事务、锁等常见面试场景。

RAG 向量检索不再绑定 PostgreSQL + pgvector，后续阶段可以根据实现需要选择 pgvector、Milvus 或其他向量库。

## What Changes

- 将主业务库从 PostgreSQL 调整为 MySQL。
- 保留 H2 作为本地开发 profile。
- 将 RAG 向量检索从固定 pgvector 调整为后续可选向量库。
- 更新项目基础规格中的技术栈要求。

## Capabilities

### New Capabilities

无。

### Modified Capabilities

- `project-foundation`: 将目标技术栈中的主数据库从 PostgreSQL + pgvector 调整为 MySQL，向量检索作为 RAG 阶段可选组件。

## Impact

- Maven runtime driver 从 PostgreSQL 替换为 MySQL。
- 新增 `application-mysql.yml`，移除 `application-postgres.yml`。
- `schema.sql` 调整为 MySQL/H2 兼容。
- README 和技术方案同步更新。

