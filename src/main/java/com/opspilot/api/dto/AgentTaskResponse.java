package com.opspilot.api.dto;

import com.opspilot.domain.task.AgentTask;
import com.opspilot.domain.task.AgentTaskStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AgentTaskResponse(
        UUID id,
        String userQuery,
        String intent,
        AgentTaskStatus status,
        String finalAnswer,
        String errorMessage,
        Instant createdAt,
        Instant updatedAt,
        List<AgentStepResponse> steps
) {
    public static AgentTaskResponse from(AgentTask task, List<AgentStepResponse> steps) {
        return new AgentTaskResponse(
                task.getId(),
                task.getUserQuery(),
                task.getIntent(),
                task.getStatus(),
                task.getFinalAnswer(),
                task.getErrorMessage(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                steps
        );
    }
}

