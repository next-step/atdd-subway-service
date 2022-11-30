package nextstep.subway.generator;

import nextstep.subway.station.domain.Station;

public class StationGenerator {

	public static Station station(String name) {
		return Station.from(name);
	}
}
