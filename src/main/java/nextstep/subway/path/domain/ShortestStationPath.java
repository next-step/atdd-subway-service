package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import nextstep.subway.station.domain.Station;

class ShortestStationPath implements StationPath {
	private final List<Station> stations;
	private final List<SectionEdge> sectionEdges;

	public ShortestStationPath(List<Station> stations, List<SectionEdge> sectionEdges) {
		this.stations = stations;
		this.sectionEdges = sectionEdges;
	}

	@Override
	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}

	@Override
	public int getTotalDistance() {
		return (int) sumTotalDistance();
	}

	private double sumTotalDistance() {
		return sectionEdges.stream().mapToDouble(SectionEdge::getWeight).sum();
	}
}
