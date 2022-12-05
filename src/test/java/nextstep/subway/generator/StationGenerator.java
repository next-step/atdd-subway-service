package nextstep.subway.generator;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;

public class StationGenerator {

	public static Station station(Long id, Name name) {
		return Station.of(id, name);
	}

	public static Station station(Name name) {
		return Station.from(name);
	}
}
