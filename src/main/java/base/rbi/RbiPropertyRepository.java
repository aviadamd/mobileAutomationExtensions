package base.rbi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
public class RbiPropertyRepository {
    private static RbiPropertyRepository repository;
    private static List<RbiProperty> allRbiUserProperties = new ArrayList<>();
    private RbiPropertyRepository() {}

    public RbiPropertyRepository getInstance() {
        if (repository == null)
            repository = new RbiPropertyRepository();
        return repository;
    }

    public List<RbiProperty> getAllRbiUsers() { return allRbiUserProperties; }

    public void addRbiUser(RbiProperty rbiProperty) {
        allRbiUserProperties.add(rbiProperty);
    }

    public void updateRbiUser(RbiProperty rbiProperty, Predicate<RbiProperty> rbiPropertyPredicate) {
        for (RbiProperty getUser: allRbiUserProperties) {
            if (rbiPropertyPredicate.test(getUser)) {
                allRbiUserProperties.remove(getUser);
                allRbiUserProperties.add(rbiProperty);
                break;
            }
        }
    }

    public void deleteBy(Predicate<RbiProperty> rbiPropertyPredicate) {
        for (RbiProperty getUser: allRbiUserProperties) {
            if (rbiPropertyPredicate.test(getUser)) {
                allRbiUserProperties.remove(getUser);
                break;
            }
        }
    }

    public void deleteAll() {
        allRbiUserProperties = new ArrayList<>();
    }

    public List<RbiProperty> getPropertyUserBy(int property) {
        List<RbiProperty> rbiProperties = new ArrayList<>();

        for (RbiProperty getUser: allRbiUserProperties) {
            if (getUser.getProperty() == property) {
                rbiProperties.add(getUser);
            }
        }

        return rbiProperties;
    }

    public Optional<RbiProperty> getPropertyUserBy(Predicate<RbiProperty> rbiPropertyPredicate) {
        for (RbiProperty getUser: allRbiUserProperties) {
            if (rbiPropertyPredicate.test(getUser)) {
                return Optional.of(getUser);
            }
        }

        return Optional.empty();
    }

    public Optional<RbiProperty> getPropertyUserBy(int property, Predicate<RbiProperty> rbiPropertyPredicate) {
        for (RbiProperty getUser: allRbiUserProperties) {
            if (getUser.getProperty() == property && rbiPropertyPredicate.test(getUser)) {
                return Optional.of(getUser);
            }
        }

        return Optional.empty();
    }

    public Optional<RbiProperty> getPropertyUserBy(int property, String vn, RbiProperty.TestStatus status) {
        for (RbiProperty getUser: allRbiUserProperties) {
            if (getUser.getProperty() == property && getUser.getUserName().equals(vn) && getUser.getTestStatus() == status) {
                return Optional.of(getUser);
            }
        }

        return Optional.empty();
    }
}
