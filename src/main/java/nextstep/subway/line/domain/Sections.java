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

import nextstep.subway.Message;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
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

		if (!stations.isEmpty() && !updateSectionStation(section)) {
			throw new RuntimeException();
		}

		sections.add(section);
	}

	public void remove(Station station) {
		validateRemovableSection();

		Optional<Section> upLineSection = findSectionByUpStation(station);
		Optional<Section> downLineSection = findSectionByDownStation(station);

		upLineSection.ifPresent(sections::remove);
		downLineSection.ifPresent(sections::remove);

		if (upLineSection.isPresent() && downLineSection.isPresent()) {
			Line line = upLineSection.get().getLine();
			Station newUpStation = downLineSection.get().getUpStation();
			Station newDownStation = upLineSection.get().getDownStation();
			int newDistance = upLineSection.get().getDistance() + downLineSection.get().getDistance();

			add(new Section(line, newUpStation, newDownStation, newDistance));
		}
	}

	private boolean updateSectionStation(Section section) {
		List<Station> stations = getStations();

		if (stations.contains(section.getUpStation())) {
			updateUpStation(section);
			return true;
		}

		if (stations.contains(section.getDownStation())) {
			updateDownStation(section);
			return true;
		}

		return false;
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
			throw new RuntimeException(Message.EXIST_SECTION);
		}

		if (!stations.isEmpty() && !stations.contains(upStation) && !stations.contains(downStation)) {
			throw new RuntimeException(Message.INVALID_SECTION);
		}
	}

	private void validateRemovableSection() {
		if (sections.size() <= 1) {
			throw new RuntimeException();
		}
	}
}
