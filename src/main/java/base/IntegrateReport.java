package base;

import base.reports.testFilters.ReasonsStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntegrateReport<T> {
    private final T action;
    private final ReasonsStep reasonsStep;

    public IntegrateReport(ReasonsStep reasonsStep, T action) {
        this.reasonsStep = reasonsStep;
        this.action = action;
    }
    public T proceed() { return this.action; }
    public Optional<ReasonsStep> getReasonsStep() { return Optional.ofNullable(this.reasonsStep); }
}
