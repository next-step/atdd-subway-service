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

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		List<Station> stations = getStations();
		throwExceptionIfNotValid(stations, upStation, downStation);
		if (stations.contains(upStation)) {
			findSectionByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
		}
		if (stations.contains(downStation)) {
			findSectionByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
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
		if (sections.isEmpty()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		Station upStation = findTopStation();
		stations.add(upStation);

		Optional<Section> nextSection;
		while ((nextSection = findSectionByUpStation(upStation)).isPresent()) {
			upStation = nextSection.map(Section::getDownStation).get();
			stations.add(upStation);
		}

		return stations;
	}

	private Station findTopStation() {
		List<Station> allStations = sections
			.stream()
			.flatMap(it -> it.getStations().stream())
			.distinct()
			.collect(Collectors.toList());

		for (Section section : sections) {
			allStations.remove(section.getDownStation());
		}
		return allStations.get(0);
	}

	public void removeStation(Line line, Station station) {
		if (sections.size() <= 1) {
			throw new IllegalArgumentException("마지막 구간은 삭제할 수 없습니다.");
		}

		Optional<Section> upSection = findSectionByUpStation(station);
		Optional<Section> downSection = findSectionByDownStation(station);

		if (upSection.isPresent() && downSection.isPresent()) {
			Station newUpStation = downSection.map(Section::getUpStation).get();
			Station newDownStation = upSection.map(Section::getDownStation).get();
			sections.add(Section.of(line, newUpStation, newDownStation, sumDistance(upSection, downSection)));
		}

		upSection.ifPresent(it -> sections.remove(it));
		downSection.ifPresent(it -> sections.remove(it));
	}

	private Optional<Section> findSectionByUpStation(Station upStation) {
		return sections.stream()
			.filter(section -> section.getUpStation().equals(upStation))
			.findAny();
	}

	private Optional<Section> findSectionByDownStation(Station downStation) {
		return sections.stream()
			.filter(section -> section.getDownStation().equals(downStation))
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
