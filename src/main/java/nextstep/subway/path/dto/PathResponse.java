package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.domain.SubwayPathStation;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
public class PathResponse {
	private List<PathStationResponse> stations;
	private int distance;

	public PathResponse(SubwayPath subwayPath) {
		this.stations = subwayPathStationsToResponses(subwayPath.getStations());
		this.distance = subwayPath.getDistance();
	}

	private List<PathStationResponse> subwayPathStationsToResponses(List<SubwayPathStation> stations) {
		return stations.stream()
			.map(before -> new PathStationResponse(before))
			.collect(Collectors.toList());
	}

	protected PathResponse() {
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
