package nextstep.subway.line.domain.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public class MiddleSectionRemoveStrategy implements SectionRemoveStrategy {

	@Override
	public void remove(Sections sections, Section sectionByUpStation, Section sectionByDownStation) {
		sectionByDownStation.extend(sectionByUpStation);
		sections.removeSection(sectionByUpStation);
	}
}
