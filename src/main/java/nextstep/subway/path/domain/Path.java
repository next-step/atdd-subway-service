package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {
	private final List<Station> stations;
	private final List<Section> sections;
	private final int distance;
	private final int fare;

	private Path(List<Station> stations, List<Section> sections, int distance, FarePolicy farePolicy) {
		this.stations = stations;
		this.sections = sections;
		this.distance = distance;
		this.fare = farePolicy.calculate(distance);
	}

	public static Path of(List<Station> stations, List<Section> sections, int distance, FarePolicy farePolicy) {
		return new Path(stations, sections, distance, farePolicy);
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}
}
