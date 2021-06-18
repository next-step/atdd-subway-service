package nextstep.subway.line.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

import java.util.ArrayList;
import java.util.Collection;
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
		return new Sections(asList(sections));
	}

	void add(Section section) {
		this.sections.add(section);
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
