package nextstep.subway.line.domain.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public class DefaultStationConnectStrategy implements SectionConnectStrategy {

	@Override
	public void connect(Sections sections, Section newSection) {
		sections.add(newSection);
	}
}
