package com.opspilot.agent.planning;

import java.util.List;

public record PlanResult(
        String intent,
        List<PlannedStep> steps
) {
}

