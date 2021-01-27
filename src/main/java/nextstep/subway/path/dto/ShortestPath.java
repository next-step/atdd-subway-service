package nextstep.subway.path.dto;

import java.util.List;

public class ShortestPath {
	private List<Long> vertexes;
	private int weight;

	public ShortestPath(List<Long> vertexes, int weight) {
		this.vertexes = vertexes;
		this.weight = weight;
	}

	public List<Long> getVertexes() {
		return vertexes;
	}

	public int getWeight() {
		return weight;
	}
}
