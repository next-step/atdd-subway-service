package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {

	private final Station source;
	private final Station target;
	private final double weight;

	public SectionEdge(Section section) {
		this.source = section.getUpStation();
		this.target = section.getDownStation();
		this.weight = section.getDistanceValue();
	}
}
