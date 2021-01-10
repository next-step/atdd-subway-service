package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public class PathFinder {

	public static void start(Station sourceStation, Station targetStation) {
		validateStation(sourceStation, targetStation);
	}

	private static void validateStation(Station sourceStation, Station targetStation) {
		if (sourceStation.equals(targetStation)) {
			throw new RuntimeException("출발역과 도착역이 동일합니다.");
		}
	}
}
