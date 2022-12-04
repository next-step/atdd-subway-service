package nextstep.subway.path.fare.policy;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarePolicies {

    private final List<FarePolicy> farePoliciesList;

    public FarePolicies(List<FarePolicy> farePoliciesList) {
        this.farePoliciesList = farePoliciesList;
    }

    public Fare calculate(Path path) {
        return farePoliciesList.stream()
                .map(farePolicy -> farePolicy.calculate(path))
                .reduce(Fare.ZERO, Fare::add);
    }

}
