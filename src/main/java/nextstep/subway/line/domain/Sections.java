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
		if (isDownStationExisted(section) && !isUpStationExisted(section)) {
			sections.stream()
				.filter(it -> it.downStation().isSameStation(section.downStation()))
				.findFirst()
				.ifPresent(it -> it.updateDownStation(section.upStation(), section.distance()));

			sections.add(section);
		}
	}

	private void addSectionWhenSameWithDownStation(Section section) {
		if (!isDownStationExisted(section) && isUpStationExisted(section)) {
			sections.stream()
				.filter(it -> it.upStation().isSameStation(section.upStation()))
				.findFirst()
				.ifPresent(it -> it.updateUpStation(section.downStation(), section.distance()));
			sections.add(section);
		}
	}

	private boolean isDownStationExisted(Section section) {
		return stations().stream().anyMatch(it -> it.isSameStation(section.downStation()));
	}

	private boolean isUpStationExisted(Section section) {
		return stations().stream().anyMatch(it -> it.isSameStation(section.upStation()));
	}

	private void validateMustHaveRelationWithExsitStation(Section section) {
		List<Station> stations = stations();
		if (!stations.isEmpty()
			&& stations.stream().noneMatch(it -> it.isSameStation(section.upStation()))
			&& stations.stream().noneMatch(it -> it.isSameStation(section.downStation()))) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private void validateIsNotDuplicated(Section section) {
		if (isUpStationExisted(section) && isDownStationExisted(section)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
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
