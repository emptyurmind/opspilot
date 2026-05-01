package com.opspilot.domain.task;

public enum AgentTaskStatus {
    CREATED,
    INTENT_RECOGNIZED,
    PLAN_GENERATED,
    TOOL_EXECUTING,
    EVIDENCE_COLLECTED,
    ANSWER_GENERATED,
    FAILED
}

