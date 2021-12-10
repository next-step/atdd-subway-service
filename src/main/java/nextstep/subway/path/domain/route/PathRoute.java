package nextstep.subway.path.domain.route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathRoute {

	private int distance;
	private List<Station> stations;
	private List<Section> sections;

	public PathRoute(int distance, List<Station> stations, List<Section> sections) {
		this.distance = distance;
		this.stations = stations;
		this.sections = sections;
	}

	public int getDistance() {
		return distance;
	}

	public List<Station> getStationsRoute() {
		return stations;
	}

	public List<Section> getSections() {
		return sections;
	}

	public Set<Line> getUsedLines() {
		return sections.stream()
			.map(it -> it.getLine())
			.collect(Collectors.toSet());
	}
}
