package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.exception.InvalidSectionsException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	private static final int MIN_SECTIONS_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public void addSection(Section newSection) {
		List<Station> stations = this.getStations();
		validateAddSection(newSection, stations);
		if (!stations.isEmpty()) {
			updateUpStation(newSection, stations);
			updateDownStation(newSection, stations);
		}
		this.sections.add(newSection);
	}

	public void removeStation(Line line, Station station) {
		validateRemoveStation(station);
		Section upSection = getSectionEqualsStation(section -> section.isEqualsDownStation(station));
		Section downSection = getSectionEqualsStation(section -> section.isEqualsUpStation(station));
		updateSectionForRemove(line, upSection, downSection);
		removeSection(upSection);
		removeSection(downSection);
	}

	public List<Station> getStations() {
		List<Station> stations = new LinkedList<>();
		if (!sections.isEmpty()) {
			stations.add(getFirstSection());
			addNextStation(stations);
		}
		return stations;
	}

	public void forEach(Consumer<? super Section> action) {
		sections.forEach(action);
	}

	private void addNextStation(List<Station> stations) {
		Section currentSection = nextSection(stations.stream().findFirst().get());
		while (Objects.nonNull(currentSection)) {
			Station currentStation = currentSection.getDownStation();
			stations.add(currentStation);
			currentSection = nextSection(currentStation);
		}
	}

	private Section nextSection(Station firstStation) {
		return sections.stream()
			.filter(section -> section.isEqualsUpStation(firstStation))
			.findFirst()
			.orElse(null);
	}

	private Station getFirstSection() {
		return sections.stream()
			.filter(section -> isFirstSection(section))
			.map(section -> section.getUpStation())
			.findFirst()
			.orElseThrow(() -> new InvalidSectionsException("첫번째 역이 존재하지 않습니다."));
	}

	private void validateNotExistedStationInSections(Section newSection, List<Station> stations) {
		if (!stations.isEmpty() && isNoneMatchSectionInSections(newSection, stations)) {
			throw new InvalidSectionsException("등록할 수 없는 구간 입니다.");
		}
	}

	private boolean isNoneMatchSectionInSections(Section newSection, List<Station> stations) {
		return isNoneMatchUpStationInSections(newSection, stations) && isNoneMatchDownStationInSections(newSection,
			stations);
	}

	private boolean isNoneMatchUpStationInSections(Section newSection, List<Station> stations) {
		return stations.stream().noneMatch(it -> newSection.isEqualsUpStation(it));
	}

	private boolean isNoneMatchDownStationInSections(Section newSection, List<Station> stations) {
		return stations.stream().noneMatch(it -> newSection.isEqualsDownStation(it));
	}

	private void validateSection(Section section) {
		if (!Objects.nonNull(section)) {
			throw new InvalidSectionsException("구간이 null 입니다.");
		}
	}

	private void validateAlreadyExistedSection(Section newSection, List<Station> stations) {
		if (newSection.isUpStationExisted(stations) && newSection.isDownStationExisted(stations)) {
			throw new InvalidSectionsException("이미 등록된 구간 입니다.");
		}
	}

	private void updateUpStation(Section newSection, List<Station> stations) {
		Section updateSection = getSectionEqualsStation(
			section -> section.isEqualsUpStation(newSection.getUpStation()));
		if (newSection.isUpStationExisted(stations) && Objects.nonNull(updateSection)) {
			updateSection.updateUpStation(newSection.getDownStation(), newSection.getDistance());
		}
	}

	private void updateDownStation(Section newSection, List<Station> stations) {
		Section updateSection = getSectionEqualsStation(
			section -> section.isEqualsDownStation(newSection.getDownStation()));
		if (newSection.isDownStationExisted(stations) && Objects.nonNull(updateSection)) {
			updateSection.updateDownStation(newSection.getUpStation(), newSection.getDistance());
		}
	}

	private void validateAddSection(Section newSection, List<Station> stations) {
		validateSection(newSection);
		validateAlreadyExistedSection(newSection, stations);
		validateNotExistedStationInSections(newSection, stations);
	}

	private void validateRemoveStation(Station station) {
		validateStation(station);
		validateMinSizeSections();
	}

	private void validateMinSizeSections() {
		if (this.sections.size() <= MIN_SECTIONS_SIZE) {
			throw new InvalidSectionsException("구간이 하나 이하이므로 지울 수 없습니다.");
		}
	}

	private void validateStation(Station station) {
		if (!Objects.nonNull(station)) {
			throw new InvalidSectionsException("역이 null 인 경우 삭제할 수 없습니다.");
		}
	}

	private void removeSection(Section section) {
		if (Objects.nonNull(section)) {
			this.sections.remove(section);
		}
	}

	private void updateSectionForRemove(Line line, Section upSection, Section downSection) {
		if (Objects.nonNull(downSection) && Objects.nonNull(upSection)) {
			Station upStation = upSection.getUpStation();
			Station downStation = downSection.getDownStation();
			Distance distance = downSection.getDistance().plus(upSection.getDistance());
			this.sections.add(new Section(line, upStation, downStation, distance));
		}
	}

	private Section getSectionEqualsStation(Predicate<Section> predicate) {
		return this.sections.stream()
			.filter(predicate::test)
			.findFirst()
			.orElse(null);
	}

	private boolean isFirstSection(Section otherSection) {
		return sections.stream().noneMatch(section -> otherSection.isEqualsUpStation(section.getDownStation()));
	}

}
