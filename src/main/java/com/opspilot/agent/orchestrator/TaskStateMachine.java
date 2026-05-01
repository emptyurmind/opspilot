package com.opspilot.agent.orchestrator;

import com.opspilot.common.exception.InvalidStateTransitionException;
import com.opspilot.domain.task.AgentTaskStatus;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TaskStateMachine {

    private final Map<AgentTaskStatus, Set<AgentTaskStatus>> transitions;

    public TaskStateMachine() {
        transitions = new EnumMap<>(AgentTaskStatus.class);
        transitions.put(AgentTaskStatus.CREATED, EnumSet.of(
                AgentTaskStatus.INTENT_RECOGNIZED,
                AgentTaskStatus.FAILED
        ));
        transitions.put(AgentTaskStatus.INTENT_RECOGNIZED, EnumSet.of(
                AgentTaskStatus.PLAN_GENERATED,
                AgentTaskStatus.FAILED
        ));
        transitions.put(AgentTaskStatus.PLAN_GENERATED, EnumSet.of(
                AgentTaskStatus.TOOL_EXECUTING,
                AgentTaskStatus.FAILED
        ));
        transitions.put(AgentTaskStatus.TOOL_EXECUTING, EnumSet.of(
                AgentTaskStatus.EVIDENCE_COLLECTED,
                AgentTaskStatus.FAILED
        ));
        transitions.put(AgentTaskStatus.EVIDENCE_COLLECTED, EnumSet.of(
                AgentTaskStatus.ANSWER_GENERATED,
                AgentTaskStatus.FAILED
        ));
        transitions.put(AgentTaskStatus.ANSWER_GENERATED, EnumSet.noneOf(AgentTaskStatus.class));
        transitions.put(AgentTaskStatus.FAILED, EnumSet.noneOf(AgentTaskStatus.class));
    }

    public void assertCanTransit(AgentTaskStatus from, AgentTaskStatus to) {
        if (!canTransit(from, to)) {
            throw new InvalidStateTransitionException("Cannot transit task status from " + from + " to " + to);
        }
    }

    public boolean canTransit(AgentTaskStatus from, AgentTaskStatus to) {
        return transitions.getOrDefault(from, Set.of()).contains(to);
    }
}

