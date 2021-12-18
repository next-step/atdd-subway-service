package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	protected Sections() {
	}

	public List<Section> get() {
		return sections;
	}

	public void add(Section other) {
		if (!sections.isEmpty()) {
			validate(other);
			updateConnectedSection(other);
		}
		sections.add(other);
	}

	public boolean isEmpty() {
		return sections.isEmpty();
	}

	private void validate(Section other) {
		List<Station> stations = getOrderedStations();
		boolean isUpStationExisted = stations.stream().anyMatch(station -> station.equals(other.getUpStation()));
		boolean isDownStationExisted = stations.stream()
			.anyMatch(station -> station.equals(other.getDownStation()));

		if (isUpStationExisted && isDownStationExisted) {
			throw Exceptions.ALREADY_EXIST_SECTION.getException();
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(station -> station.equals(other.getUpStation())) &&
			stations.stream().noneMatch(station -> station.equals(other.getDownStation()))) {
			throw Exceptions.NO_CONNECTED_SECTION.getException();
		}
	}

	private void updateConnectedSection(Section other) {
		getUpLineSection(other.getUpStation())
			.ifPresent(section -> section.updateUpStation(other.getDownStation(), other.getDistance()));
		getDownLineSection(other.getDownStation())
			.ifPresent(section -> section.updateDownStation(other.getUpStation(), other.getDistance()));
	}

	public List<Station> getOrderedStations() {
		Station downStation = findUpStation();
		List<Station> stations = new ArrayList<>();

		while (downStation != null) {
			stations.add(downStation);
			Optional<Section> nextLineSection = findNextSection(downStation);
			downStation = nextLineSection.map(Section::getDownStation).orElse(null);
		}
		return stations;
	}

	private Station findUpStation() {
		if (sections.size() == 0) {
			return null;
		}

		Station searchStation = sections.get(0).getUpStation();
		Station upStation = searchStation;

		while (searchStation != null) {
			upStation = searchStation;
			Optional<Section> prevLineSection = findPrevSection(searchStation);
			searchStation = prevLineSection.map(Section::getUpStation).orElse(null);
		}
		return upStation;
	}

	private Optional<Section> findNextSection(Station station) {
		return sections.stream()
			.filter(section -> section.getUpStation() == station)
			.findFirst();
	}

	private Optional<Section> findPrevSection(Station station) {
		return sections.stream()
			.filter(section -> section.getDownStation() == station)
			.findFirst();
	}

	public void removeByStation(Station station) {
		validateOnRemove();

		Optional<Section> upLineSection = getUpLineSection(station);
		Optional<Section> downLineSection = getDownLineSection(station);
		if (upLineSection.isPresent() && downLineSection.isPresent()) {
			addSectionOnExistTargetInside(upLineSection.get(), downLineSection.get());
		}
		upLineSection.ifPresent(section -> sections.remove(section));
		downLineSection.ifPresent(section -> sections.remove(section));
	}

	private void addSectionOnExistTargetInside(Section upLineSection, Section downLineSection) {
		Station newUpStation = downLineSection.getUpStation();
		Station newDownStation = upLineSection.getDownStation();
		int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
		sections.add(new Section(upLineSection.getLine(), newUpStation, newDownStation, newDistance));
	}

	private Optional<Section> getDownLineSection(Station station) {
		return sections.stream()
			.filter(section -> section.getDownStation().equals(station))
			.findFirst();
	}

	private Optional<Section> getUpLineSection(Station station) {
		return sections.stream()
			.filter(section -> section.getUpStation().equals(station))
			.findFirst();
	}

	private void validateOnRemove() {
		if (sections.size() <= 1) {
			throw Exceptions.SECTION_MUST_BE_EXIST.getException();
		}
	}

	public List<Line> getLinesDistinct() {
		return sections.stream()
			.map(Section::getLine)
			.distinct()
			.collect(Collectors.toList());
	}

	public boolean contains(Station station) {
		return sections.stream()
			.anyMatch(section -> section.contains(station));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Sections sections1 = (Sections)o;
		return Objects.equals(sections, sections1.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sections);
	}
}
