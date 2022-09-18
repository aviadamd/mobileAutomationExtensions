package base.repository.mongo.adapters;

import base.reports.testFilters.ReasonsStep;
import org.bson.Document;
import java.util.List;
import java.util.stream.Collectors;

public class ReasonsStepDtoAdapter {

    private ReasonsStepDtoAdapter() {}

    public static Document toDocument(final ReasonsStep reasons) {
        return DocumentAdapter.toDocument(reasons
                .append("status", reasons.getStatus().getName())
                .append("stepId", reasons.getStepId())
                .append("testName", reasons.getCategory().getText())
                .append("category", reasons.getSeverity().getText())
                .append("description", reasons.getDescription()));
    }

    public static List<Document> toDocuments(final List<ReasonsStep> reportTestDto) {
        return reportTestDto
                .stream()
                .map(reasons -> DocumentAdapter.toDocument(reasons
                        .append("status", reasons.getStatus().getName())
                        .append("stepId", reasons.getStepId())
                        .append("testName", reasons.getCategory().getText())
                        .append("category", reasons.getSeverity().getText())
                        .append("description", reasons.getDescription())))
                .collect(Collectors.toList());
    }
}
