package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FindShortestPathResult {
	private GraphPath<Station, DefaultWeightedEdge> path;

	public FindShortestPathResult(GraphPath<Station, DefaultWeightedEdge> path) {
		this.path = path;
	}

	public List<StationResponse> getStations() {
		return path.getVertexList()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	public int getDistance() {
		return (int)path.getWeight();
	}
}
