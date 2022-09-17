package base;

import base.repository.ReportStepRepository;

public class IntegrateListReports<T> {
    private final T action;
    private ReportStepRepository repositoryReportStep;
    public IntegrateListReports(ReportStepRepository repositoryReportStep, T action) {
        this.repositoryReportStep = repositoryReportStep;
        this.action = action;
    }
    public T proceed() { return this.action; }
    public ReportStepRepository getRepositoryReportStep() { return this.repositoryReportStep;}
}
