package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private PathFinder() {

	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder();
	}

	public Path find(Station source, Station target) {
		return null;
	}
}
