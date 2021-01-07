package nextstep.subway.path.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
	public void registerLines(List<Line> lines) {
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
		return new ShortestPath();
	}
}
