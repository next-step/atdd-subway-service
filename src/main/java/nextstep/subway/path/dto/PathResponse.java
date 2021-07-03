package nextstep.subway.path.dto;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

	private List<StationResponse> stations;
	private int distance;
	private int extraFee;

	public PathResponse(List<StationResponse> stations, int distance, int extraFee) {
		this.stations = stations;
		this.distance = distance;
		this.extraFee = extraFee;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}

	public static PathResponse of(GraphPath<Station, SectionEdge> path) {
		//todo 계산하자
		int extraFee = 0;
		return new PathResponse(StationResponse.of(path.getVertexList()), (int)path.getWeight(), extraFee);
	}
}
