package com.opspilot.infrastructure.repository;

import com.opspilot.domain.task.AgentStep;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentStepRepository extends JpaRepository<AgentStep, UUID> {

    List<AgentStep> findByTaskIdOrderByStepOrderAsc(UUID taskId);
}

