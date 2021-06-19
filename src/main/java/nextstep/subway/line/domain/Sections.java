package nextstep.subway.line.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {}

	private Sections(List<Section> sections) {
		this.sections = sections;
	}

	static Sections of(Section... sections) {
		return new Sections(new ArrayList<>(asList(sections)));
	}

	void add(Section other) {
		validateNonDuplication(other);
		validateContainStation(other);
		connectSection(other);
		sections.add(other);
	}

	private void connectSection(Section other) {
		for (Section section : sections) {
			section.connectIfAdjacent(other);
		}
	}

	private void validateNonDuplication(Section other) {
		boolean isUpStationExisted = sections.stream()
			.anyMatch(section -> section.hasEqualUpStation(other));

		boolean isDownStationExisted = sections.stream()
			.anyMatch(section -> isUpStationExisted && section.hasEqualDownStation(other));

		if (isUpStationExisted && isDownStationExisted) {
			throw new IllegalArgumentException("이미 등록된 구간 입니다.");
		}
	}

	private void validateContainStation(Section otherSection) {
		if (sections.isEmpty()) {
			return;
		}
		boolean notContainStation = sections.stream()
			.flatMap(Section::getStations)
			.noneMatch(otherSection::contain);
		if (notContainStation) {
			throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
		}
	}

	List<Section> getSections() {
		return sections;
	}

	List<Station> getStationsInOrder() {
		return getSectionsInOrder()
			.flatMap(Section::getStations)
			.distinct()
			.collect(toList());
	}

	private Stream<Section> getSectionsInOrder() {
		Section section = sections.get(0);
		Stream<Section> upwardSections = getUpwardSectionsClosed(section);
		Stream<Section> downwardSections = getDownwardSectionsClosed(section);
		return concat(upwardSections, downwardSections).distinct();
	}

	private Stream<Section> getUpwardSectionsClosed(Section currentSection) {
		Optional<Section> upwardSection = sections.stream()
			.filter(section -> section.isUpwardOf(currentSection))
			.findAny();

		Stream<Section> current = Stream.of(currentSection);

		return upwardSection
			.map(upward -> concat(getUpwardSectionsClosed(upward), current))
			.orElse(current);
	}

	private Stream<Section> getDownwardSectionsClosed(Section currentSection) {
		Optional<Section> downwardSection = sections.stream()
			.filter(section -> section.isDownwardOf(currentSection))
			.findAny();

		Stream<Section> current = Stream.of(currentSection);

		return downwardSection
			.map(downward -> concat(current, getDownwardSectionsClosed(downward)))
			.orElse(current);
	}
}
