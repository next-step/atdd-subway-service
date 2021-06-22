package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class SectionEdge extends DefaultWeightedEdge {
	private final transient Section section;

	SectionEdge(Section section) {
		this.section = section;
	}

	Station getUpStation() {
		return section.getUpStation();
	}

	Station getDownStation() {
		return section.getDownStation();
	}

	int getDistance() {
		return section.getDistance();
	}
}
