package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface AdditionalFarePolicy {
    int addFare(Sections sections, List<Station> paths);
}
