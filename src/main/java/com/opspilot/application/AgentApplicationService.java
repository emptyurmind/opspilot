package com.opspilot.application;

import com.opspilot.agent.planning.PlanResult;
import com.opspilot.agent.planning.PlannedStep;
import com.opspilot.agent.planning.RuleBasedPlanningService;
import com.opspilot.api.dto.AgentStepResponse;
import com.opspilot.api.dto.AgentTaskResponse;
import com.opspilot.common.exception.ResourceNotFoundException;
import com.opspilot.domain.task.AgentStep;
import com.opspilot.domain.task.AgentTask;
import com.opspilot.infrastructure.repository.AgentStepRepository;
import com.opspilot.infrastructure.repository.AgentTaskRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentApplicationService {

    private final AgentTaskRepository taskRepository;
    private final AgentStepRepository stepRepository;
    private final RuleBasedPlanningService planningService;

    public AgentApplicationService(
            AgentTaskRepository taskRepository,
            AgentStepRepository stepRepository,
            RuleBasedPlanningService planningService
    ) {
        this.taskRepository = taskRepository;
        this.stepRepository = stepRepository;
        this.planningService = planningService;
    }

    @Transactional
    public AgentTaskResponse createTask(String query) {
        PlanResult plan = planningService.plan(query);
        AgentTask task = taskRepository.save(new AgentTask(query, plan.intent()));
        List<AgentStep> steps = plan.steps().stream()
                .map(step -> toAgentStep(task.getId(), step))
                .toList();
        List<AgentStep> savedSteps = stepRepository.saveAll(steps);
        return AgentTaskResponse.from(task, toStepResponses(savedSteps));
    }

    @Transactional(readOnly = true)
    public AgentTaskResponse getTask(UUID taskId) {
        AgentTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent task not found: " + taskId));
        List<AgentStep> steps = stepRepository.findByTaskIdOrderByStepOrderAsc(taskId);
        return AgentTaskResponse.from(task, toStepResponses(steps));
    }

    private AgentStep toAgentStep(UUID taskId, PlannedStep step) {
        return new AgentStep(
                taskId,
                step.order(),
                step.stepName(),
                step.toolName(),
                step.paramsJson()
        );
    }

    private List<AgentStepResponse> toStepResponses(List<AgentStep> steps) {
        return steps.stream()
                .map(AgentStepResponse::from)
                .toList();
    }
}

