package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new LinkedList<>();

	public Sections() {
	}

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	public void add(Section section) {
		sections.add(section);
	}

	public boolean isEmpty() {
		return sections.isEmpty();
	}



	public Station findFirstStation() {
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

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findFirstStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> it.isUpStationEqualTo(finalDownStation))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return stations;
	}

	public void addSection(Section section) {
		boolean isUpStationExisted = isUpStationExisted(section.getUpStation());
		boolean isDownStationExisted = isDownStationExisted(section.getDownStation());

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!isStationsEmpty() && isUpStationNoneMatch(section.getUpStation()) &&
			isDownStationNoneMatch(section.getDownStation())) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (isStationsEmpty()) {
			sections.add(section);
			return;
		}

		if (!isUpStationExisted && !isDownStationExisted) {
			throw new RuntimeException("구간을 추가할 수 없습니다.");
		}

		if (isUpStationExisted) {
			sections.stream()
				.filter(it -> it.isUpStationEqualTo(section.getUpStation()))
				.findFirst()
				.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

			sections.add(section);
		}

		if (isDownStationExisted) {
			sections.stream()
				.filter(it -> it.isDownStationEqualTo(section.getDownStation()))
				.findFirst()
				.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

			sections.add(section);
		}
	}

	private boolean isStationsEmpty() {
		return getStations().isEmpty();
	}

	private boolean isDownStationNoneMatch(Station downStation) {
		return getStations().stream().noneMatch(it -> it == downStation);
	}

	private boolean isUpStationNoneMatch(Station upStation) {
		return getStations().stream().noneMatch(it -> it == upStation);
	}

	private boolean isDownStationExisted(Station downStation) {
		return getStations().stream().anyMatch(it -> it == downStation);
	}

	private boolean isUpStationExisted(Station upStation) {
		return getStations().stream().anyMatch(it -> it == upStation);
	}

	public void deleteSection(Station station) {
		if (sections.size() <= 1) {
			throw new RuntimeException("구간이 하나밖에 없어 삭제할 수 없습니다.");
		}

		Optional<Section> upSection = sections.stream()
			.filter(it -> it.isDownStationEqualTo(station))
			.findFirst();
		Optional<Section> downSection = sections.stream()
			.filter(it -> it.isUpStationEqualTo(station))
			.findFirst();

		if (upSection.isPresent() && downSection.isPresent()) {
			Station newUpStation = downSection.get().getUpStation();
			Station newDownStation = upSection.get().getDownStation();
			int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
			sections.add(new Section(upSection.get().getLine(), newUpStation, newDownStation, newDistance));
		}

		upSection.ifPresent(it -> sections.remove(it));
		downSection.ifPresent(it -> sections.remove(it));
	}
}
