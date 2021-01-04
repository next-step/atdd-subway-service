package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
class LineSections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections;

	LineSections() {
		sections = new ArrayList<>();
	}

	LineSections(Section section) {
		this();
		this.sections.add(section);
	}

	List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = findUpStationEqual(finalDownStation);
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return stations;
	}

	void addLineStation(Section section) {
		List<Station> stations = this.getStations();
		boolean isUpStationExisted = stations.stream().anyMatch(section::upStationEquals);
		boolean isDownStationExisted = stations.stream().anyMatch(section::downStationEquals);

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(section::upStationEquals) &&
				stations.stream().noneMatch(section::downStationEquals)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			this.sections.add(section);
			return;
		}

		if (isUpStationExisted) {
			findUpStationEqual(section.getUpStation())
					.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

			this.sections.add(section);
		} else if (isDownStationExisted) {
			findDownStationEqual(section.getDownStation())
					.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

			this.sections.add(section);
		} else {
			throw new RuntimeException();
		}
	}

	private Station findUpStation() {
		Station downStation = this.sections.get(0).getUpStation();
	    while (downStation != null) {
	        Station finalDownStation = downStation;
	        Optional<Section> nextLineStation = findDownStationEqual(finalDownStation);
	        if (!nextLineStation.isPresent()) {
	            break;
	        }
	        downStation = nextLineStation.get().getUpStation();
	    }

	    return downStation;
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
	        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
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
