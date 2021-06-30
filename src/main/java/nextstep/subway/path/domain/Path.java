package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

	private final List<Line> lines;

	public Path(List<Line> lines) {
		this.lines = lines;
	}

	public List<Station> getNodes() {
		return lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.collect(Collectors.toList());
	}
}
