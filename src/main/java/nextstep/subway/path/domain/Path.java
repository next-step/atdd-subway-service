package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {
	private final List<Station> stations;
	private final List<Section> sections;
	private final int distance;

	private Path(List<Station> stations, List<Section> sections, int distance) {
		this.stations = stations;
		this.sections = sections;
		this.distance = distance;
	}

	public static Path of(List<Station> stations, List<Section> sections, int distance) {
		return new Path(stations, sections, distance);
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
