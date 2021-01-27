package nextstep.subway.station.domain;

import java.util.Collections;
import java.util.List;

public class Stations {
	private List<Station> stations;

	public Stations(List<Station> stations) {
		this.stations = stations;
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}
}
