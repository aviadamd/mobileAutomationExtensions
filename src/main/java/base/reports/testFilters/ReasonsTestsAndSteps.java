package base.reports.testFilters;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

public class ReasonsTestsAndSteps extends BasicDBObject {

    static final long serialVersionUID = 2105061907470199595L;

    private Reasons reasons;
    private static List<ReasonsStep> reasonsListStep = new ArrayList<>();

    public ReasonsTestsAndSteps() {}

    public void initTest(Reasons reasons) {
        this.reasons = reasons;
    }

    public void addStep(ReasonsStep reasonsStep) {
        reasonsListStep.add(reasonsStep);
    }
}
