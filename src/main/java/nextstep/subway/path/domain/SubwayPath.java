package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
public class SubwayPath {
	private List<SubwayPathStation> stations;
	private int distance;

	public SubwayPath(List<SubwayPathStation> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public List<SubwayPathStation> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public List<Long> getStationsIds() {
		return stations.stream()
			.map(SubwayPathStation::getId)
			.collect(Collectors.toList());
	}
}
