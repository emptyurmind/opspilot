package com.opspilot.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent_step")
public class AgentStep {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "step_order", nullable = false)
    private int stepOrder;

    @Column(name = "step_name", nullable = false, length = 200)
    private String stepName;

    @Column(name = "tool_name", length = 120)
    private String toolName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private AgentStepStatus status;

    @Column(name = "params_json", columnDefinition = "text")
    private String paramsJson;

    @Column(name = "result_json", columnDefinition = "text")
    private String resultJson;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AgentStep() {
    }

    public AgentStep(UUID taskId, int stepOrder, String stepName, String toolName, String paramsJson) {
        this.taskId = taskId;
        this.stepOrder = stepOrder;
        this.stepName = stepName;
        this.toolName = toolName;
        this.paramsJson = paramsJson;
        this.status = AgentStepStatus.PENDING;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public String getStepName() {
        return stepName;
    }

    public String getToolName() {
        return toolName;
    }

    public AgentStepStatus getStatus() {
        return status;
    }

    public String getParamsJson() {
        return paramsJson;
    }

    public String getResultJson() {
        return resultJson;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void start() {
        this.status = AgentStepStatus.RUNNING;
        this.startedAt = Instant.now();
    }

    public void succeed(String resultJson) {
        this.status = AgentStepStatus.SUCCESS;
        this.resultJson = resultJson;
        this.finishedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = AgentStepStatus.FAILED;
        this.errorMessage = errorMessage;
        this.finishedAt = Instant.now();
    }
}
