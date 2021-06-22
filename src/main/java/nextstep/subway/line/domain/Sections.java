package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

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
		Optional<Section> upLineStation = getSectionOptionalEqualsUpStation(station);
		Optional<Section> downLineStation = getSectionOptionalEqualsDownStation(station);
		updateSectionForRemove(line, upLineStation, downLineStation);
		removeSection(upLineStation, downLineStation);
	}

	public List<Station> getStations() {
		List<Station> stations = new LinkedList<>();
		if (findFirstSectionOptional().isPresent()) {
			stations.add(findFirstSectionOptional().get().getUpStation());
			addNextStation(stations);
		}
		return stations;
	}

	private void addNextStation(List<Station> stations) {
		Optional<Section> currentOptionalSection = nextStationOptional(stations.stream().findFirst().get());
		while (currentOptionalSection.isPresent()) {
			Station currentStation = currentOptionalSection.get().getDownStation();
			stations.add(currentStation);
			currentOptionalSection = nextStationOptional(currentStation);
		}
	}

	private Optional<Section> nextStationOptional(Station firstStation) {
		return sections.stream()
			.filter(section -> section.isEqualsUpStation(firstStation))
			.findFirst();
	}

	private Optional<Section> findFirstSectionOptional() {
		return sections.stream()
			.filter(section -> isFirstSection(section))
			.findFirst();
	}

	private void validateNotExistedStationInSections(Section newSection, List<Station> stations) {
		if (!stations.isEmpty() && isNoneMatchSectionInSections(newSection, stations)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private boolean isNoneMatchSectionInSections(Section newSection, List<Station> stations) {
		return isNoneMatchUpStationInSections(newSection, stations) && isNoneMatchDownStationInSections(newSection, stations);
	}

	private boolean isNoneMatchUpStationInSections(Section newSection, List<Station> stations) {
		return stations.stream().noneMatch(it -> newSection.isEqualsUpStation(it));
	}

	private boolean isNoneMatchDownStationInSections(Section newSection, List<Station> stations) {
		return stations.stream().noneMatch(it -> newSection.isEqualsDownStation(it));
	}

	private void validateSection(Section section) {
		if(!Objects.nonNull(section)) {
			throw new RuntimeException("구간이 null 입니다.");
		}
	}

	private void validateAlreadyExistedSection(Section newSection, List<Station> stations) {
		if (newSection.isUpStationExisted(stations) && newSection.isDownStationExisted(stations)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	private void updateUpStation(Section newSection, List<Station> stations) {
		if (newSection.isUpStationExisted(stations)) {
			getSectionOptionalEqualsUpStation(newSection.getUpStation())
				.ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
		}
	}

	private void updateDownStation(Section newSection, List<Station> stations) {
		if (newSection.isDownStationExisted(stations)) {
			getSectionOptionalEqualsDownStation(newSection.getDownStation())
				.ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
		}
	}

	private void validateAddSection(Section newSection, List<Station> stations)
	{
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
			throw new RuntimeException("구간이 하나 이하이므로 지울 수 없습니다.");
		}
	}

	private void validateStation(Station station) {
		if(!Objects.nonNull(station)) {
			throw new RuntimeException("역이 null 인 경우 삭제할 수 없습니다.");
		}
	}

	private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
		upLineStation.ifPresent(it -> this.sections.remove(it));
		downLineStation.ifPresent(it -> this.sections.remove(it));
	}

	private void updateSectionForRemove(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
			this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
		}
	}

	private Optional<Section> getSectionOptionalEqualsDownStation(Station station) {
		return this.sections.stream()
			.filter(section -> section.isEqualsDownStation(station))
			.findFirst();
	}

	private Optional<Section> getSectionOptionalEqualsUpStation(Station station) {
		return this.sections.stream()
			.filter(section -> section.isEqualsUpStation(station))
			.findFirst();
	}

	private boolean isFirstSection(Section otherSection) {
		return sections.stream().noneMatch(section -> otherSection.isEqualsUpStation(section.getDownStation()));
	}


}
