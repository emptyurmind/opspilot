package com.opspilot.agent.planning;

import com.opspilot.common.exception.UnsupportedIntentException;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class RuleBasedPlanningService {

    public PlanResult plan(String userQuery) {
        if (isFiveHundredIncident(userQuery)) {
            return new PlanResult("INCIDENT_5XX_DIAGNOSIS", List.of(
                    new PlannedStep(1, "查询错误日志", "LocalLogSearchTool", "{}"),
                    new PlannedStep(2, "查询错误率监控", "LocalMetricQueryTool", "{}"),
                    new PlannedStep(3, "查询发布变更", "LocalGitChangeTool", "{}"),
                    new PlannedStep(4, "检索故障手册", "LocalKnowledgeSearchTool", "{}"),
                    new PlannedStep(5, "生成排障报告", "AnswerComposer", "{}")
            ));
        }
        throw new UnsupportedIntentException("当前仅支持 500、报错、异常类排障问题，后续版本会扩展慢 SQL 和依赖超时场景。");
    }

    private boolean isFiveHundredIncident(String userQuery) {
        String normalized = userQuery.toLowerCase(Locale.ROOT);
        return normalized.contains("500")
                || normalized.contains("报错")
                || normalized.contains("异常");
    }
}

