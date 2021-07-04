package nextstep.subway.path.dto;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int fare;

	public PathResponse(List<StationResponse> stations, int distance, int fare) {
		this.stations = stations;
		this.distance = distance;
		this.fare = fare;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}

	public static PathResponse of(GraphPath<Station, SectionEdge> path, Fare fare) {
		return new PathResponse(StationResponse.of(path.getVertexList()), (int)path.getWeight(), fare.value());
	}
}
