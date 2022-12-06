package nextstep.subway.line.domain.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public interface SectionRemoveStrategy {
	void remove(Sections sections, Section sectionByUpStation, Section sectionByDownStation);
}
