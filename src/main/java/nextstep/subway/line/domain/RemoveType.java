package nextstep.subway.line.domain;

import java.util.function.BinaryOperator;

public enum RemoveType {

	FIRST((byUpStation, byDownStation) -> byDownStation),
	MIDDLE(Section::removeMiddleSection),
	LAST((byUpStation, byDownStation) -> byUpStation);

	private final BinaryOperator<Section> expression;

	RemoveType(BinaryOperator<Section> expression) {
		this.expression = expression;
	}

	public static RemoveType match(Section sectionByUpStation, Section sectionByDownStation) {
		if (isMiddleSection(sectionByUpStation, sectionByDownStation)) {
			return MIDDLE;
		}
		if (isLastSection(sectionByUpStation, sectionByDownStation)) {
			return LAST;
		}
		return FIRST;
	}

	private static boolean isMiddleSection(Section upSection, Section downSection) {
		return upSection != null && downSection != null;
	}

	private static boolean isLastSection(Section upSection, Section downSection) {
		return upSection != null && downSection == null;
	}

	public void remove(Sections sections, Section sectionByUpStation, Section sectionByDownStation) {
		Section toRemove = expression.apply(sectionByUpStation, sectionByDownStation);
		sections.removeSection(toRemove);
	}
}
