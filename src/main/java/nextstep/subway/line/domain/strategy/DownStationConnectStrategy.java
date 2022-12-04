package nextstep.subway.line.domain.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

public class DownStationConnectStrategy implements SectionConnectStrategy {

	private final Section existingSection;

	public DownStationConnectStrategy(Section existingSection) {
		this.existingSection = existingSection;
	}

	@Override
	public void connect(Sections sections, Section newSection) {
		rearrange(newSection);
		sections.add(newSection);
	}

	private void rearrange(Section newSection) {
		existingSection.replaceDownStation(newSection);
	}
}
