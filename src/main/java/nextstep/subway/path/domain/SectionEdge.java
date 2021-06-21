package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class SectionEdge extends DefaultWeightedEdge {
	private final transient Section section;

	SectionEdge(Section section) {
		this.section = section;
	}

	@Override
	protected double getWeight() {
		return section.getDistance();
	}

	@Override
	public Station getSource() {
		return section.getUpStation();
	}

	@Override
	public Station getTarget() {
		return section.getDownStation();
	}
}
