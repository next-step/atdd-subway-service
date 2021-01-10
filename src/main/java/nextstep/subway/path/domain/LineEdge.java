package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

class LineEdge extends DefaultWeightedEdge {
	private Station sourceVertex;
	private Station targetVertex;
	private Distance distance;

	LineEdge(Station sourceVertex, Station targetVertex, Distance distance) {
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.distance = distance;
	}

	@Override
	public final Station getSource() {
		return sourceVertex;
	}

	@Override
	public final Station getTarget() {
		return targetVertex;
	}

	public Distance getDistance() {
		return distance;
	}

	@Override
	protected double getWeight() {
		return distance.getWeight();
	}
}
