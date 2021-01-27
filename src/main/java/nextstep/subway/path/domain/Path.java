package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
	public static final String UNCONNECTED_SOURCE_AND_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";
	private final List<Station> stations;
	private final Distance distance;
	private final Fare fare;

	public Path(List<Station> stations, int distance, List<Line> lines) {
		validate(stations);
		this.stations = Collections.unmodifiableList(stations);
		this.distance = new Distance(distance);
		this.fare = new Fare(distance, lines);
	}

	private void validate(List<Station> stations) {
		if (Objects.isNull(stations) || stations.isEmpty()) {
			throw new IllegalArgumentException(UNCONNECTED_SOURCE_AND_TARGET);
		}
	}

	public static Path of(List<Station> stations, double weight, List<Line> lines) {
		return new Path(stations, (int)weight, lines);
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance.value();
	}

	public int getFare() {
		return fare.value();
	}
}
