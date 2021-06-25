package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private final List<Station> stations;
	private final List<SectionEdge> sectionEdges;

	Path(List<Station> stations, List<SectionEdge> sectionEdges) {
		this.stations = stations;
		this.sectionEdges = sectionEdges;
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}

	public int sumTotalDistance() {
		return sectionEdges.stream()
			.mapToInt(SectionEdge::getDistance)
			.sum();
	}
}
