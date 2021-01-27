package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Path {
	private final List<Station> stations;
	private final long distance;
	private final Fare fare;

	public Path(Sections sections, List<Station> stations, long distance) {
		this.stations = stations;
		this.distance = distance;
		this.fare = new Fare(sections, stations, distance);
	}

	public Path discountFareByAge(Integer age) {
		fare.discountByAge(age);
		return this;
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public long getFare() {
		return fare.getFare();
	}
}
