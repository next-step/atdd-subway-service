package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Distance;

import java.util.List;

public interface FarePolicy {
    void calculateFare(Fare fare, Distance distance, List<Section> sections);
}
