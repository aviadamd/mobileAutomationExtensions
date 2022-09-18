package base.repository;

import base.reports.testFilters.Reasons;
import base.reports.testFilters.ReasonsStep;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ReportStepRepository {

    private static ReportStepRepository mInstance = null;
    public static ReportStepRepository getInstance() {
        if (mInstance == null) {
            mInstance = new ReportStepRepository();
        }
        return mInstance;
    }

    private static List<ReasonsStep> reportTestRepositoryList = new ArrayList<>();
    public List<ReasonsStep> getAllObjects() { return reportTestRepositoryList; }
    public void save(ReasonsStep testReportDto) { reportTestRepositoryList.add(testReportDto); }
    public void saveAll(List<ReasonsStep> reportStepDto) {
        reportTestRepositoryList.addAll(reportStepDto);
    }
    public void deleteAll() {
        reportTestRepositoryList = new ArrayList<>();
    }

    public void delete(String id) {
        for (ReasonsStep reportStepDto: reportTestRepositoryList) {
            if (this.findObjectBy(e -> e.getStepId().equals(id)).isPresent()) {
                reportTestRepositoryList.remove(reportStepDto);
                break;
            }
        }
    }

    public void updateTestStatus(String id, Status testStatus) {
        for (ReasonsStep reportDto: reportTestRepositoryList) {
            if (this.findObjectBy(e -> e.getStepId().equals(id)).isPresent()) {
                reportDto.setStatus(testStatus);
                reportTestRepositoryList.add(reportDto);
                break;
            }
        }
    }

    public ReasonsStep getLastObject() {
        return reportTestRepositoryList
                .stream()
                .reduce((first,second) -> second).orElse(null);
    }

    public Optional<ReasonsStep> findObjectById(String id) {
        for (ReasonsStep reasonsStep : Optional.of(reportTestRepositoryList
                .stream()
                .filter(reportStepDto -> reportStepDto.getStepId().equals(id))
                .collect(Collectors.toList()))
                .get()) {
            return Optional.of(reasonsStep);
        }
        return Optional.empty();
    }

    public Optional<ReasonsStep> findObjectBy(Predicate<ReasonsStep> dtoPredicate) {
        List<ReasonsStep> list = new ArrayList<>();
        for (ReasonsStep reasonsStep : reportTestRepositoryList) {
            if (dtoPredicate.test(reasonsStep)) {
                list.add(reasonsStep);
            }
        }

        return Optional.of(list)
                .get()
                .stream()
                .findFirst();
    }

    public Optional<List<ReasonsStep>> findObjectsBy(Predicate<ReasonsStep> dtoPredicate) {
        return Optional.of(reportTestRepositoryList
                .stream()
                .filter(dtoPredicate)
                .collect(Collectors.toList()));
    }


    public void print(Reasons testReportDto) {
        if (testReportDto != null) {
            log.debug(testReportDto.toString());
        }
    }

    public void printAll() {
        if (reportTestRepositoryList != null && !reportTestRepositoryList.isEmpty()) {
            reportTestRepositoryList.forEach(report -> {
                log.debug(report.toString());
            });
        }
    }
}
