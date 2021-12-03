package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {
	private List<Station> stations;

	private Path() {

	}

	public Path(List<Station> stations) {
		this.stations = stations;
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return 0;
	}
}
