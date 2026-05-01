package com.opspilot.agent.planning;

public record PlannedStep(
        int order,
        String stepName,
        String toolName,
        String paramsJson
) {
}

