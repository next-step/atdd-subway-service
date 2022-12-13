package nextstep.subway.line.domain;

public class SectionRemover {

	private SectionRemover() {
	}

	public static void remove(Sections sections, Section sectionByUpStation, Section sectionByDownStation) {
		RemoveType.match(sectionByUpStation, sectionByDownStation)
			.remove(sections, sectionByUpStation, sectionByDownStation);
	}

}
