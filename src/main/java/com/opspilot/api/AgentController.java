package com.opspilot.api;

import com.opspilot.api.dto.AgentTaskResponse;
import com.opspilot.api.dto.CreateAgentTaskRequest;
import com.opspilot.application.AgentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Agent Tasks", description = "Agent 排障任务接口")
@RestController
@RequestMapping("/api/agent/tasks")
public class AgentController {

    private final AgentApplicationService agentApplicationService;

    public AgentController(AgentApplicationService agentApplicationService) {
        this.agentApplicationService = agentApplicationService;
    }

    @Operation(summary = "创建 Agent 排障任务")
    @PostMapping
    public ResponseEntity<AgentTaskResponse> createTask(@Valid @RequestBody CreateAgentTaskRequest request) {
        AgentTaskResponse response = agentApplicationService.createTask(request.query());
        return ResponseEntity
                .created(URI.create("/api/agent/tasks/" + response.id()))
                .body(response);
    }

    @Operation(summary = "查询 Agent 排障任务详情")
    @GetMapping("/{taskId}")
    public AgentTaskResponse getTask(@PathVariable UUID taskId) {
        return agentApplicationService.getTask(taskId);
    }
}

