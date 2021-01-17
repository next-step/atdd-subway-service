package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private List<Station> stations;
	private long distance;

	public Path(List<Station> stations, long distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}
}
