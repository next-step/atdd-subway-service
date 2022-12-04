package nextstep.subway.line.domain;

import nextstep.subway.line.domain.strategy.FirstSectionRemoveStrategy;
import nextstep.subway.line.domain.strategy.LastSectionRemoveStrategy;
import nextstep.subway.line.domain.strategy.MiddleSectionRemoveStrategy;
import nextstep.subway.line.domain.strategy.SectionRemoveStrategy;

public class SectionRemover {

	private final SectionRemoveStrategy removeStrategy;

	private SectionRemover(Section sectionByUpStation, Section sectionByDownStation) {
		this.removeStrategy = strategy(sectionByUpStation, sectionByDownStation);
	}

	public static SectionRemover of(Section sectionByUpStation, Section sectionByDownStation) {
		return new SectionRemover(sectionByUpStation, sectionByDownStation);
	}

	public void remove(Sections sections, Section sectionByUpStation, Section sectionByDownStation) {
		removeStrategy.remove(sections, sectionByUpStation, sectionByDownStation);
	}

	private SectionRemoveStrategy strategy(
		Section sectionByUpStation,
		Section sectionByDownStation
	) {
		if (isMiddleSection(sectionByUpStation, sectionByDownStation)) {
			return new MiddleSectionRemoveStrategy();
		}
		if (isLastSection(sectionByUpStation, sectionByDownStation)) {
			return new LastSectionRemoveStrategy();
		}
		return new FirstSectionRemoveStrategy();
	}

	private boolean isMiddleSection(Section upSection, Section downSection) {
		return upSection != null && downSection != null;
	}

	private boolean isLastSection(Section upSection, Section downSection) {
		return upSection != null && downSection == null;
	}

}
