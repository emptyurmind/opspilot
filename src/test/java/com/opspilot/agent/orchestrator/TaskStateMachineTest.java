package com.opspilot.agent.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.opspilot.common.exception.InvalidStateTransitionException;
import com.opspilot.domain.task.AgentTaskStatus;
import org.junit.jupiter.api.Test;

class TaskStateMachineTest {

    private final TaskStateMachine stateMachine = new TaskStateMachine();

    @Test
    void allowsLegalTransition() {
        assertThat(stateMachine.canTransit(
                AgentTaskStatus.CREATED,
                AgentTaskStatus.INTENT_RECOGNIZED
        )).isTrue();
    }

    @Test
    void rejectsIllegalTransition() {
        assertThatThrownBy(() -> stateMachine.assertCanTransit(
                AgentTaskStatus.CREATED,
                AgentTaskStatus.ANSWER_GENERATED
        )).isInstanceOf(InvalidStateTransitionException.class);
    }
}

