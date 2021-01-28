package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class LineWeightedEdge extends DefaultWeightedEdge {
	private Long lineId;

	public LineWeightedEdge(Long lineId) {
		this.lineId = lineId;
	}

	public Long getLineId() {
		return lineId;
	}
}
