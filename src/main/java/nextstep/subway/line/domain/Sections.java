package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
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
	public static final String EXIST_SECTION = "이미 등록된 구간 입니다.";
	public static final String INVALID_SECTION = "등록할 수 없는 구간 입니다.";
	public static final String INVALID_SECTION_SIZE = "구간을 지울 수 없습니다.";

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private final List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(
			sections.stream()
				.sorted()
				.map(Section::getStations)
				.flatMap(Collection::stream)
				.distinct()
				.collect(Collectors.toList())
		);
	}

	public void add(Section section) {
		List<Station> stations = getStations();
		validateAddableSection(stations, section);

		updateSectionStation(stations, section);

		sections.add(section);
	}

	public void remove(Station station) {
		validateRemovableSection();

		Optional<Section> upLineSection = findSectionByUpStation(station);
		Optional<Section> downLineSection = findSectionByDownStation(station);

		upLineSection.ifPresent(sections::remove);
		downLineSection.ifPresent(sections::remove);

		if (upLineSection.isPresent() && downLineSection.isPresent()) {
			connectSections(upLineSection.get(), downLineSection.get());
		}
	}

	private void connectSections(Section upSection, Section downSection) {
		Line line = upSection.getLine();
		Station newUpStation = downSection.getUpStation();
		Station newDownStation = upSection.getDownStation();
		int newDistance = upSection.getDistance() + downSection.getDistance();

		add(new Section(line, newUpStation, newDownStation, newDistance));
	}

	private void updateSectionStation(List<Station> stations, Section section) {
		if (stations.contains(section.getUpStation())) {
			updateUpStation(section);
			return;
		}

		if (stations.contains(section.getDownStation())) {
			updateDownStation(section);
		}
	}

	private void updateUpStation(Section section) {
		findSectionByUpStation(section.getUpStation())
			.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
	}

	private void updateDownStation(Section section) {
		findSectionByDownStation(section.getDownStation())
			.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
	}

	public Optional<Section> findSectionByUpStation(Station upStation) {
		return sections.stream()
			.filter(it -> it.isUpStation(upStation))
			.findFirst();
	}

	public Optional<Section> findSectionByDownStation(Station downStation) {
		return sections.stream()
			.filter(it -> it.isDownStation(downStation))
			.findFirst();
	}

	private void validateAddableSection(List<Station> stations, Section section) {
		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();

		if (stations.contains(upStation) && stations.contains(downStation)) {
			throw new IllegalArgumentException(EXIST_SECTION);
		}

		if (!stations.isEmpty() && !stations.contains(upStation) && !stations.contains(downStation)) {
			throw new IllegalArgumentException(INVALID_SECTION);
		}
	}

	private void validateRemovableSection() {
		if (sections.size() <= 1) {
			throw new IllegalArgumentException(INVALID_SECTION_SIZE);
		}
	}
}
