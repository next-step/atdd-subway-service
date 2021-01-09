package nextstep.subway.line.domain;

import nextstep.subway.line.application.ValidationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
class LineSections implements Iterable<Section> {

	private static final int MINIMUM_SECTION_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections;

	@Override
	public Iterator<Section> iterator() {
		return this.sections.iterator();
	}

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
			throw new ValidationException("이미 등록된 구간 입니다.");
		}

		if (!(isUpStationExisted || isDownStationExisted)) {
			throw new ValidationException("등록할 수 없는 구간 입니다.");
		}

		if (isUpStationExisted) {
			findUpStationEqual(section.getUpStation()).ifPresent(ifSection -> ifSection.updateUpStation(section));
			sections.add(section);
			return;
		}

		findDownStationEqual(section.getDownStation()).ifPresent(ifSection -> ifSection.updateDownStation(section));
		sections.add(section);
	}

	void removeLineStation(Station station) {
		if (this.sections.size() <= MINIMUM_SECTION_SIZE) {
			throw new ValidationException("최소 구간 사이즈일때 구간 삭제가 불가능합니다.");
		}

		Optional<Section> upSection = findUpStationEqual(station);
		Optional<Section> downSection = findDownStationEqual(station);
		if (upSection.isPresent() && downSection.isPresent()) {
			this.sections.add(merge(upSection.get(), downSection.get()));
		}

		upSection.ifPresent(it -> this.sections.remove(it));
		downSection.ifPresent(it -> this.sections.remove(it));
	}

	private Section merge(Section upSection, Section downSection) {
		Station newUpStation = downSection.getUpStation();
		Station newDownStation = upSection.getDownStation();
		Distance newDistance = upSection.getDistance().plus(downSection.getDistance());
		return new Section(upSection.getLine(), newUpStation, newDownStation, newDistance);
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
