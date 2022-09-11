package base.repository.mongo.adapters;

import base.reports.testFilters.Reasons;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import java.util.List;
import java.util.stream.Collectors;

public class ReasonsDtoAdapter extends BasicDBObject {

    private ReasonsDtoAdapter() {}

    public static Document toDocument(final Reasons reasons) {
        return DocumentAdapter.toDocument(reasons
                .append("status", reasons.getTestStatus().getName())
                .append("_id", reasons.getTestId())
                .append("testName", reasons.getTestName())
                .append("category", reasons.getCategory().getText())
                .append("severity", reasons.getSeverity().getText())
                .append("reason", reasons.getDescription()));
    }

    public static Document toDocumentWithSteps(final Reasons reasons) {
        return DocumentAdapter.toDocument(reasons
                .append("status", reasons.getTestStatus().getName())
                .append("_id", reasons.getTestId())
                .append("testName", reasons.getTestName())
                .append("category", reasons.getCategory().getText())
                .append("severity", reasons.getSeverity().getText())
                .append("reason", reasons.getDescription())
                .append("reasonSteps", reasons.getReasonsStep().toString()));
    }

    public static List<Document> toDocuments(final List<Reasons> reportTestDto) {
        return reportTestDto
                .stream()
                .map(reasons -> DocumentAdapter.toDocument(reasons
                        .append("status", reasons.getTestStatus().getName())
                        .append("_id", reasons.getTestId())
                        .append("testName", reasons.getTestName())
                        .append("category", reasons.getCategory().getText())
                        .append("severity", reasons.getSeverity().getText())
                        .append("reason", reasons.getDescription())))
                .collect(Collectors.toList());
    }
}
