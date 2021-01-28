package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.LineWeightedEdge;

public class ShortestPath {
	private List<Long> lineIds;
	private List<Long> stationIds;
	private int distance;

	public ShortestPath(List<LineWeightedEdge> edges, List<Long> stationIds, int distance) {
		this.lineIds = map(edges);
		this.stationIds = stationIds;
		this.distance = distance;
	}

	private List<Long> map(List<LineWeightedEdge> edges) {
		return edges.stream()
			.map(LineWeightedEdge::getLineId)
			.collect(Collectors.toList());
	}

	public List<Long> getLineIds() {
		return lineIds;
	}

	public List<Long> getStationIds() {
		return stationIds;
	}

	public int getDistance() {
		return distance;
	}
}
