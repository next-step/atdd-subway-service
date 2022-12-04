package nextstep.subway.line.domain.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public interface SectionConnectStrategy {
	void connect(Sections sections, Section section);
}
