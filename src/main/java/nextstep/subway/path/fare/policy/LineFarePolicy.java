package nextstep.subway.path.fare.policy;

import nextstep.subway.path.fare.Fare;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LineFarePolicy {

    public Fare calculate(List<Station> stations) {
        return Fare.ZERO;
    }
}
