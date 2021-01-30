package nextstep.subway.path.dto;

import java.util.StringJoiner;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {
	private Section section;

	public void setSection(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}

	public int getLineExtraFare() {
		Line line = section.getLine();
		if(line == null) {
			return 0;
		}
		return line.getExtraFare();
	}
}
