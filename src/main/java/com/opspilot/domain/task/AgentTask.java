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
@Table(name = "agent_task")
public class AgentTask {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "user_query", nullable = false, length = 2000)
    private String userQuery;

    @Column(name = "intent", length = 80)
    private String intent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private AgentTaskStatus status;

    @Column(name = "final_answer", columnDefinition = "text")
    private String finalAnswer;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "metadata_json", columnDefinition = "text")
    private String metadataJson;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AgentTask() {
    }

    public AgentTask(String userQuery, String intent) {
        this.userQuery = userQuery;
        this.intent = intent;
        this.status = AgentTaskStatus.CREATED;
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

    public String getUserQuery() {
        return userQuery;
    }

    public String getIntent() {
        return intent;
    }

    public AgentTaskStatus getStatus() {
        return status;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void transitionTo(AgentTaskStatus status) {
        this.status = status;
    }

    public void markFailed(String errorMessage) {
        this.status = AgentTaskStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void complete(String finalAnswer) {
        this.status = AgentTaskStatus.ANSWER_GENERATED;
        this.finalAnswer = finalAnswer;
    }
}
