package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.station.dto.StationResponse;

public class PathFinderResponse {

	private final List<StationResponse> stationResponses;
	private final List<SectionEdge> shortestPathEdges;
	private final Distance distance;
	private final SubwayFare subwayFare;

	public PathFinderResponse(List<StationResponse> stationResponses,
		List<SectionEdge> shortestPathEdges, Distance distance, SubwayFare subwayFare) {
		this.stationResponses = stationResponses;
		this.shortestPathEdges = shortestPathEdges;
		this.distance = distance;
		this.subwayFare = subwayFare;
	}

	public List<StationResponse> getStationResponses() {
		return stationResponses;
	}

	public List<SectionEdge> getShortestPathEdges() {
		return shortestPathEdges;
	}

	public Distance getDistance() {
		return distance;
	}

	public SubwayFare getSubwayFare() {
		return subwayFare;
	}
}
