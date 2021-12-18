package nextstep.subway.path.domain.shortest;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {

	private final Line line;
	private final Station sourceVertex;
	private final Station targetVertex;
	private final int distance;

	private SectionEdge(Line line, Station sourceVertex, Station targetVertex, int distance) {
		this.line = line;
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.distance = distance;
	}

	public static SectionEdge of(Section section) {
		return new SectionEdge(
			section.getLine(),
			section.getUpStation(),
			section.getDownStation(),
			section.getDistance()
		);
	}

	@Override
	protected Object getSource() {
		return sourceVertex;
	}

	@Override
	protected Object getTarget() {
		return targetVertex;
	}

	@Override
	protected double getWeight() {
		return distance;
	}

	public Line getLine() {
		return line;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SectionEdge)) {
			return false;
		}
		SectionEdge that = (SectionEdge)o;
		return distance == that.distance && line == that.line && Objects.equals(sourceVertex,
			that.sourceVertex) && Objects.equals(targetVertex, that.targetVertex);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceVertex, targetVertex, distance, line);
	}
}
