package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

class LineEdge extends DefaultWeightedEdge {
	private Station sourceVertex;
	private Station targetVertex;
	private Distance distance;
	private Fare lineFare;

	public static LineEdge of(Section section) {
		return new LineEdge(section.getUpStation(), section.getDownStation(), section.getDistance(), section.getLine().getFare());
	}

	LineEdge(Station sourceVertex, Station targetVertex, Distance distance, Fare lineFare) {
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.distance = distance;
		this.lineFare = lineFare;
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

	public Fare getLineFare() {
		return lineFare;
	}
}
