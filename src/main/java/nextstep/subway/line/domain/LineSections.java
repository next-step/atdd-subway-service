package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
class LineSections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections;

	LineSections() {
		sections = new ArrayList<>();
	}

	LineSections(Section section) {
		this();
		addLineStation(section);
	}

	List<Station> getStations() {
		Optional<Station> downStation = findUpStation();
		if (!downStation.isPresent()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		while (downStation.isPresent()) {
			stations.add(downStation.get());
			downStation = findUpStationEqual(downStation.get())
					.map(Section::getDownStation);
		}
		return stations;
	}

	private Optional<Station> findUpStation() {
		Optional<Section> nextSection = this.sections.stream().findFirst();
		Optional<Section> firstSection = nextSection;

		while (nextSection.isPresent()) {
			firstSection = nextSection;
			nextSection = findDownStationEqual(nextSection.get().getUpStation());
		}

		return firstSection.map(Section::getUpStation);
	}

	void addLineStation(Section section) {
		if (this.sections.isEmpty()) {
			this.sections.add(section);
			return;
		}

		List<Station> stations = this.getStations();
		boolean isUpStationExisted = stations.stream().anyMatch(section::upStationEquals);
		boolean isDownStationExisted = stations.stream().anyMatch(section::downStationEquals);

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!(isUpStationExisted || isDownStationExisted)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (isUpStationExisted) {
			findUpStationEqual(section.getUpStation()).ifPresent(if_section -> if_section.updateUpStation(section));
			sections.add(section);
			return;
		}

		findDownStationEqual(section.getDownStation()).ifPresent(if_section -> if_section.updateDownStation(section));
		sections.add(section);
	}

	void removeLineStation(Station station) {
		if (this.sections.size() <= 1) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = findUpStationEqual(station);
		Optional<Section> downLineStation = findDownStationEqual(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
			this.sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> this.sections.remove(it));
		downLineStation.ifPresent(it -> this.sections.remove(it));
	}

	private Optional<Section> findUpStationEqual(Station station) {
		return this.sections.stream()
				.filter(it -> it.upStationEquals(station))
				.findFirst();
	}

	private Optional<Section> findDownStationEqual(Station station) {
		return this.sections.stream()
				.filter(it -> it.downStationEquals(station))
				.findFirst();
	}
}
