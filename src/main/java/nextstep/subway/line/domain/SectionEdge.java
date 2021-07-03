package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

	private Section section;

	public SectionEdge(Section section) {
		this.section = section;
	}

}
