package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.MinimumSectionException;
import nextstep.subway.station.domain.Station;

@Getter
@Embeddable
@NoArgsConstructor
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public void initSection(Section section) {
		sections.add(section);
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
				.filter(it -> it.getUpStation().equals(finalDownStation))
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
		Station downStation = this.sections.get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.sections.stream()
				.filter(it -> it.getDownStation().equals(finalDownStation))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	public void addSection(Section section) {
		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();

		List<Station> stations = getStations();

		boolean isUpStationExisted = stations.contains(upStation);
		boolean isDownStationExisted = stations.contains(downStation);
		AddSectionType addSectionType = AddSectionType.findAddSectionType(isUpStationExisted, isDownStationExisted);
		addSectionType.addSection(section, this.sections);
	}

	public void removeLineStation(Line line, Station station) {
		validateBeforeRemove();
		Optional<Section> upLineStation = this.sections.stream()
			.filter(it -> it.getUpStation().equals(station))
			.findFirst();
		Optional<Section> downLineStation = this.sections.stream()
			.filter(it -> it.getDownStation().equals(station))
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

	private void validateBeforeRemove() {
		if (this.sections.size() <= 1) {
			throw new MinimumSectionException("최소 1개 이상의 구간이 필요합니다.");
		}
	}
}
