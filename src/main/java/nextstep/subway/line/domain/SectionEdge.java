package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {

	private final Line line;
	private final Station upStation;
	private final Station downStation;

	public SectionEdge(final Line line, final Station upStation, final Station downStation) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
	}

	public SectionEdge(final Section section) {
		this(section.getLine(), section.getUpStation(), section.getDownStation());
	}

	public Line getLine() {
		return line;
	}

}
