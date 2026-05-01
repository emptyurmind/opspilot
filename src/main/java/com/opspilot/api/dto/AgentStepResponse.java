package com.opspilot.api.dto;

import com.opspilot.domain.task.AgentStep;
import com.opspilot.domain.task.AgentStepStatus;
import java.time.Instant;
import java.util.UUID;

public record AgentStepResponse(
        UUID id,
        int order,
        String stepName,
        String toolName,
        AgentStepStatus status,
        String paramsJson,
        String resultJson,
        String errorMessage,
        Instant startedAt,
        Instant finishedAt
) {
    public static AgentStepResponse from(AgentStep step) {
        return new AgentStepResponse(
                step.getId(),
                step.getStepOrder(),
                step.getStepName(),
                step.getToolName(),
                step.getStatus(),
                step.getParamsJson(),
                step.getResultJson(),
                step.getErrorMessage(),
                step.getStartedAt(),
                step.getFinishedAt()
        );
    }
}

