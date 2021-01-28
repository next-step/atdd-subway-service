package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private final List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public Sections(List<Section> sections) {
		this.sections.addAll(sections);
	}

	public static Sections merge(List<Line> lines) {
		Sections merged = new Sections();
		lines.stream()
			.map(Line::getSections)
			.forEach(merged::addAllByDropDuplicate);
		return merged;
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public List<Section> getSections(Line line) {
		List<Section> sectionsByLine = sections.stream()
			.filter(section -> section.getLine().equals(line))
			.collect(Collectors.toList());
		return Collections.unmodifiableList(sectionsByLine);
	}

	public Sections addAllByDropDuplicate(Sections other) {
		for (Section section : other.getSections()) {
			try {
				addSection(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance());
			} catch (IllegalArgumentException e) {}
		}
		return this;
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		List<Station> stations = getSortedStations(line);
		throwExceptionIfNotValid(stations, upStation, downStation);
		if (stations.contains(upStation)) {
			findSectionByUpStation(line, upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
		}
		if (stations.contains(downStation)) {
			findSectionByDownStation(line, downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
		}
		sections.add(Section.of(line, upStation, downStation, distance));
	}

	private void throwExceptionIfNotValid(List<Station> stations, Station upStation, Station downStation) {
		if (stations.contains(upStation) && stations.contains(downStation)) {
			throw new IllegalArgumentException("이미 등록된 구간 입니다.");
		}
		if (!stations.isEmpty() && !stations.contains(upStation) && !stations.contains(downStation)) {
			throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
		}
	}

	public List<Station> getStations() {
		return sections.stream()
			.flatMap(it -> it.getStations().stream())
			.distinct()
			.collect(Collectors.toList());
	}

	public List<Station> getSortedStations(Line line) {
		if (sections.isEmpty()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		findTopStation(line).ifPresent(upStation -> {
			stations.add(upStation);

			Optional<Section> nextSection;
			while ((nextSection = findSectionByUpStation(line, upStation)).isPresent()) {
				upStation = nextSection.map(Section::getDownStation).get();
				stations.add(upStation);
			}
		});
		return stations;
	}

	private Optional<Station> findTopStation(Line line) {
		List<Section> sectionsByLine = getSections(line);
		if (sectionsByLine.isEmpty()) {
			return Optional.empty();
		}
		List<Station> allStations = sectionsByLine.stream()
			.flatMap(it -> it.getStations().stream())
			.distinct()
			.collect(Collectors.toList());

		for (Section section : sectionsByLine) {
			allStations.remove(section.getDownStation());
		}
		return Optional.ofNullable(allStations.get(0));
	}

	public void removeStation(Line line, Station station) {
		if (sections.size() <= 1) {
			throw new IllegalArgumentException("마지막 구간은 삭제할 수 없습니다.");
		}

		Optional<Section> upSection = findSectionByUpStation(line, station);
		Optional<Section> downSection = findSectionByDownStation(line, station);

		if (upSection.isPresent() && downSection.isPresent()) {
			Station newUpStation = downSection.map(Section::getUpStation).get();
			Station newDownStation = upSection.map(Section::getDownStation).get();
			sections.add(Section.of(line, newUpStation, newDownStation, sumDistance(upSection, downSection)));
		}

		upSection.ifPresent(sections::remove);
		downSection.ifPresent(sections::remove);
	}

	public Optional<Line> findLineWithMinDistance(Station upStation, Station downStation) {
		return sections.stream()
			.filter(section -> section.getStations().containsAll(Arrays.asList(upStation, downStation)))
			.reduce((section1, section2) -> {
				if (section1.getDistance() < section2.getDistance()) {
					return section1;
				}
				return section2;
			})
			.map(Section::getLine);
	}

	private Optional<Section> findSectionByUpStation(Line line, Station upStation) {
		return sections.stream()
			.filter(section -> section.getLine().equals(line) && section.getUpStation().equals(upStation))
			.findAny();
	}

	private Optional<Section> findSectionByDownStation(Line line, Station downStation) {
		return sections.stream()
			.filter(section -> section.getLine().equals(line) && section.getDownStation().equals(downStation))
			.findAny();
	}

	private int sumDistance(Optional<Section>... sections) {
		return Arrays.stream(sections)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.mapToInt(Section::getDistance)
			.sum();
	}
}
