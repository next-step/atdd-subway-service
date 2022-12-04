package nextstep.subway.generator;

import nextstep.subway.station.domain.Station;

public class StationGenerator {

	public static Station station(Long id, String name) {
		return Station.of(id, name);
	}
}
