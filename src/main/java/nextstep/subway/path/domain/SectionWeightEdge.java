package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.line.domain.Section;

public class SectionWeightEdge extends DefaultWeightedEdge {

	private final Section section;

	private SectionWeightEdge(Section section) {
		Assert.notNull(section, "구간은 필수입니다.");
		this.section = section;
	}

	public static SectionWeightEdge from(Section section) {
		return new SectionWeightEdge(section);
	}

	public Section section() {
		return section;
	}

	@Override
	protected double getWeight() {
		return section.getDistance();
	}

	@Override
	protected Object getSource() {
		return section.getUpStation();
	}

	@Override
	protected Object getTarget() {
		return section.getDownStation();
	}
}
