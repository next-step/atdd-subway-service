package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	private static final int MINIMUM_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public Sections(Section section) {
		sections.add(section);
	}

	public List<Section> value() {
		return sections;
	}

	public List<Station> stations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}
		return arrangeStationsFromSections();
	}

	private List<Station> arrangeStationsFromSections() {
		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> finalDownStation.isSameStation(it.upStation()))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().downStation();
			stations.add(downStation);
		}

		return stations;
	}

	private Station findUpStation() {
		Station downStation = sections.get(0).upStation();
		while (downStation != null) {
			Station finalUpStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> finalUpStation.isSameStation(it.downStation()))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().upStation();
		}

		return downStation;
	}

	public void addLineStation(Section section) {
		validateIsNotDuplicated(section);
		validateMustHaveRelationWithExsitStation(section);

		addSectionWhenEmptySection(section);
		addSectionWhenSameWithDownStation(section);
		addSectionWhenSameWithUpStation(section);
	}

	private void addSectionWhenEmptySection(Section section) {
		if (sections.isEmpty()) {
			sections.add(section);
		}
	}

	private void addSectionWhenSameWithUpStation(Section section) {
		if (isStationExisted(section.downStation()) && !isStationExisted(section.upStation())) {
			sections.stream()
				.filter(it -> it.downStation().isSameStation(section.downStation()))
				.findFirst()
				.ifPresent(it -> it.updateDownStation(section.upStation(), section.distance()));

			sections.add(section);
		}
	}

	private void addSectionWhenSameWithDownStation(Section section) {
		if (!isStationExisted(section.downStation()) && isStationExisted(section.upStation())) {
			sections.stream()
				.filter(it -> it.upStation().isSameStation(section.upStation()))
				.findFirst()
				.ifPresent(it -> it.updateUpStation(section.downStation(), section.distance()));
			sections.add(section);
		}
	}

	private boolean isStationExisted(Station station) {
		return stations().stream().anyMatch(station::isSameStation);
	}

	private boolean isStationNotExisted(Station station) {
		return stations().stream().noneMatch(it -> it.isSameStation(station));
	}

	private void validateMustHaveRelationWithExsitStation(Section section) {
		if (!stations().isEmpty()
				&& isStationNotExisted(section.upStation())
				&& isStationNotExisted(section.downStation())) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private void validateIsNotDuplicated(Section section) {
		if (isStationExisted(section.upStation()) && isStationExisted(section.downStation())) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	public void removeLineStation(Line line, Station station) {
		validateMinimunLineStation();

		Optional<Section> upLineStation = findSectionSameUpStation(station);
		Optional<Section> downLineStation = findSectionSameDownStation(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			sections.add(new Section(line, upLineStation, downLineStation));
		}
		removePreviousSections(upLineStation, downLineStation);
	}

	private void removePreviousSections(Optional<Section> upLineStation, Optional<Section> downLineStation) {
		upLineStation.ifPresent(it -> sections.remove(it));
		downLineStation.ifPresent(it -> sections.remove(it));
	}

	private Optional<Section> findSectionSameDownStation(Station station) {
		return sections.stream()
			.filter(it -> it.downStation().equals(station))
			.findFirst();
	}

	private Optional<Section> findSectionSameUpStation(Station station) {
		return sections.stream()
			.filter(it -> it.upStation().equals(station))
			.findFirst();
	}

	private void validateMinimunLineStation() {
		if (sections.size() <= MINIMUM_SIZE) {
			throw new RuntimeException("최소 1개의 구간은 존재해야 합니다.");
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Sections)) {
			return false;
		}
		Sections that = (Sections)object;
		return sections.containsAll(that.sections)
			&& that.sections.containsAll(sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sections);
	}
}
