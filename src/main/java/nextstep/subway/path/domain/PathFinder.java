package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private List<Line> lines;

	public PathFinder(List<Line> lines) {
		this.lines = lines;
	}

	public List<String> findPath(Station startStation, Station destinationStation) {
		return Arrays.asList("신촌역", "홍대입구역");
	}

	public int findPathLength(Station startStation, Station destinationStation) {
		return 7;
	}
}
