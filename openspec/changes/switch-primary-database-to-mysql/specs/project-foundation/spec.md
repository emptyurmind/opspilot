## MODIFIED Requirements

### Requirement: 技术栈边界
系统 SHALL 以 Java 17、Spring Boot 3.x、LangChain4j、MySQL、Redis、SSE 和 Docker Compose 作为目标主技术栈，并在 RAG 阶段根据需要引入 pgvector、Milvus 或其他向量检索组件。

#### Scenario: 技术栈可追溯
- **WHEN** 开发者查看项目基础规格
- **THEN** 规格 MUST 明确 MySQL 是主业务库，向量检索组件属于 RAG 阶段可选能力

