package nextstep.subway.fare.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public interface Fare {

	void calculateFare(List<Station> path, List<Section> sections, int distance);

	int getFare();
}
