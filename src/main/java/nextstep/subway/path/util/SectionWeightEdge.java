package nextstep.subway.path.util;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;

public class SectionWeightEdge extends DefaultWeightedEdge {

	private Section section;

	public SectionWeightEdge(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}
}
