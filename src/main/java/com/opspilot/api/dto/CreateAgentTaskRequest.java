package com.opspilot.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAgentTaskRequest(
        @NotBlank
        @Size(max = 2000)
        String query
) {
}

