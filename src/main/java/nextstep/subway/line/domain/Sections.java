package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public void addSection(Section section) {
		List<Station> stations = this.getStations();
		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
			stations.stream().noneMatch(it -> it == section.getDownStation())) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			this.sections.add(section);
			return;
		}

		if (isUpStationExisted) {
			this.sections.stream()
				.filter(it -> it.getUpStation() == section.getUpStation())
				.findFirst()
				.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

			this.sections.add(section);
		} else if (isDownStationExisted) {
			this.sections.stream()
				.filter(it -> it.getDownStation() == section.getDownStation())
				.findFirst()
				.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

			this.sections.add(section);
		} else {
			throw new RuntimeException();
		}
	}

	public List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.sections.stream()
				.filter(it -> it.getUpStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return stations;
	}

	private Station findUpStation() {
		Station downStation = sections.get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> it.getDownStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	public void removeStation(Line line, Station station) {
		if (this.sections.size() <= 1) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = this.sections.stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst();
		Optional<Section> downLineStation = this.sections.stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> this.sections.remove(it));
		downLineStation.ifPresent(it -> this.sections.remove(it));
	}
}
