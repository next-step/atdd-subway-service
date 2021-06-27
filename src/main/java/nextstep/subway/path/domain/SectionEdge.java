package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class SectionEdge extends DefaultWeightedEdge {
	private final transient Section section;
	private final transient Fare lineExtraFare;

	SectionEdge(Section section, Fare lineExtraFare) {
		this.section = section;
		this.lineExtraFare = lineExtraFare;
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

	Fare getLineExtraFare() {
		return lineExtraFare;
	}
}
