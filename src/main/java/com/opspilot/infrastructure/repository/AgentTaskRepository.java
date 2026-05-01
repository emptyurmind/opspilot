package com.opspilot.infrastructure.repository;

import com.opspilot.domain.task.AgentTask;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentTaskRepository extends JpaRepository<AgentTask, UUID> {
}

