# OpsPilot

OpsPilot is an intelligent incident diagnosis agent for microservice engineering teams.

The project is designed as a Java backend system that demonstrates agent orchestration,
tool calling, RAG-based knowledge retrieval, evidence tracking, and execution auditing.

## Project Goal

In microservice systems, incident diagnosis often requires engineers to switch between
logs, metrics, release records, Git changes, runbooks, and tickets. OpsPilot aims to
aggregate these sources through an agent workflow and generate traceable diagnosis
reports.

## Documentation

- [Technical Design](docs/technical-design.md)

## Planned Tech Stack

- Java 17
- Spring Boot 3.x
- LangChain4j
- PostgreSQL + pgvector
- Redis
- Server-Sent Events
- Docker Compose

