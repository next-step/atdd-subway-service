package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarePolicies {

    private final List<FarePolicy> farePolicyList;

    public FarePolicies(List<FarePolicy> farePolicyList) {
        this.farePolicyList = farePolicyList;
    }

    public Fare calculate(Distance distance) {
        return farePolicyList.stream()
                .map(farePolicy -> farePolicy.calculateFare(distance))
                .reduce(Fare.ZERO, Fare::add);
    }

    public Fare calculate(int distance) {
        return calculate(Distance.valueOf(distance));
    }

    public Fare calculate(Path path) {
        Fare distanceFare = getDistanceFare(path);
        Fare lineFare = getLineFare(path);

        return distanceFare.add(lineFare);
    }

    private Fare getLineFare(Path path) {
        List<Station> stations = path.getStations();
        return Fare.ZERO;
    }

    private Fare getDistanceFare(Path path) {
        Distance distance = path.getDistance();
        return calculate(distance);
    }
}
