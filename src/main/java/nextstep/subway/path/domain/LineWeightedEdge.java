package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;

public class LineWeightedEdge extends DefaultWeightedEdge {

	private Line line;

	protected LineWeightedEdge() {
	}

	private LineWeightedEdge(Line line) {
		this.line = line;
	}

	public static LineWeightedEdge from(Line line) {
		return new LineWeightedEdge(line);
	}

	public Line getLine() {
		return line;
	}
}
