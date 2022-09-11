package base.repository;

import base.reports.testFilters.Reasons;
import com.aventstack.extentreports.Status;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ReportTestRepository {

    private static ReportTestRepository mInstance = null;
    public static ReportTestRepository getInstance() {
        if (mInstance == null) {
            mInstance = new ReportTestRepository();
        }
        return mInstance;
    }

    private static List<Reasons> reportTestRepositoryList = new ArrayList<>();
    public List<Reasons> getAllObjects() { return reportTestRepositoryList; }
    public void save(Reasons testReportDto) {
        reportTestRepositoryList.add(testReportDto);
    }
    public void saveAll(List<Reasons> reportStepDto) {
        reportTestRepositoryList.addAll(reportStepDto);
    }
    public void deleteAll() {
        reportTestRepositoryList = new ArrayList<>();
    }

    public void delete(String id) {
        for (Reasons reportStepDto: reportTestRepositoryList) {
            if (this.findObjectBy(e -> e.getTestId().equals(id)).isPresent()) {
                reportTestRepositoryList.remove(reportStepDto);
                break;
            }
        }
    }

    public void updateTestStatus(String id, Status testStatus) {
        for (Reasons reportDto: reportTestRepositoryList) {
            if (this.findObjectBy(e -> e.getTestId().equals(id)).isPresent()) {
                reportDto.setTestStatus(testStatus);
                reportTestRepositoryList.add(reportDto);
                break;
            }
        }
    }

    public Reasons getLastObject() {
        return reportTestRepositoryList
                .stream()
                .reduce((first,second) -> second).orElse(null);
    }

    public Optional<Reasons> findObjectById(String id) {
        return Optional.of(reportTestRepositoryList
                .stream()
                .filter(reportStepDto -> reportStepDto.getTestId().equals(id))
                .collect(Collectors.toList()))
                .get()
                .stream()
                .findFirst();
    }

    public Optional<Reasons> findObjectBy(Predicate<Reasons> dtoPredicate) {
        return Optional.of(reportTestRepositoryList
                .stream()
                .filter(dtoPredicate)
                .collect(Collectors.toList()))
                .get()
                .stream()
                .findFirst();
    }

    public Optional<List<Reasons>> findObjectsBy(Predicate<Reasons> dtoPredicate) {
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
