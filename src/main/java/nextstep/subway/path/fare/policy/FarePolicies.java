package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarePolicies {

    private final DistanceFarePolicies distanceFarePolicies;
    private final LineFarePolicy lineFarePolicy;

    public FarePolicies(DistanceFarePolicies distanceFarePolicies, LineFarePolicy lineFarePolicy) {
        this.distanceFarePolicies = distanceFarePolicies;
        this.lineFarePolicy = lineFarePolicy;
    }

    public Fare calculate(Path path) {
        Fare distanceFare = getDistanceFare(path);
        Fare lineFare = getLineFare(path);
        return distanceFare.add(lineFare);
    }

    private Fare getLineFare(Path path) {
        List<Station> stations = path.getStations();
        return lineFarePolicy.calculate(stations);
    }

    private Fare getDistanceFare(Path path) {
        Distance distance = path.getDistance();
        return distanceFarePolicies.calculate(distance);
    }
}
