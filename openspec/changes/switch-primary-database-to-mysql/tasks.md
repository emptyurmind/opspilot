## 1. 配置调整

- [x] 1.1 将 Maven runtime driver 从 PostgreSQL 替换为 MySQL
- [x] 1.2 新增 `application-mysql.yml`
- [x] 1.3 移除 `application-postgres.yml`
- [x] 1.4 将 H2 local profile 调整为 MySQL compatibility mode

## 2. Schema 调整

- [x] 2.1 将 UUID 字段调整为 `varchar(36)`
- [x] 2.2 将时间字段调整为 MySQL/H2 兼容的 `timestamp`
- [x] 2.3 保留任务步骤外键和索引

## 3. 文档与规格

- [x] 3.1 更新 README 技术栈
- [x] 3.2 更新技术方案中的数据库和向量检索描述
- [x] 3.3 更新 OpenSpec project-foundation 技术栈要求

## 4. 验证

- [x] 4.1 运行 OpenSpec validate
- [ ] 4.2 Maven 依赖可用后运行测试
