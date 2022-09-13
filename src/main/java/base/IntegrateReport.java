package base;

import base.reports.testFilters.ReasonsStep;

public class IntegrateReport<T> {

    private final T action;
    private final ReasonsStep reasonsStep;

    public IntegrateReport(ReasonsStep reasonsStep, T action) {
        this.reasonsStep = reasonsStep;
        this.action = action;
    }

    public T proceed() { return action; }
    public ReasonsStep getReasonsStep() { return reasonsStep; }
}
