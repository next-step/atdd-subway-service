package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {
	public static final String UNCONNECTED_SOURCE_AND_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";
	private final List<Station> stations;
	private final Distance distance;

	public Path(List<Station> stations, int distance) {
		validate(stations);
		this.stations = Collections.unmodifiableList(stations);
		this.distance = new Distance(distance);
	}

	private void validate(List<Station> stations) {
		if (Objects.isNull(stations) || stations.isEmpty()) {
			throw new IllegalArgumentException(UNCONNECTED_SOURCE_AND_TARGET);
		}
	}

	public static Path of(List<Station> stations, double weight) {
		return new Path(stations, (int)weight);
	}

	private static void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
		if (Objects.isNull(graphPath)) {
			throw new IllegalArgumentException(UNCONNECTED_SOURCE_AND_TARGET);
		}
	}

	public List<Station> getStations() {
		return stations;
	}

	public Distance getDistance() {
		return distance;
	}
}
