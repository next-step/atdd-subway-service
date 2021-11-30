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

	protected Sections() {
	}

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	public List<Station> getOrderedStations() {
		if (isEmptySections()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
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

	private boolean isEmptySections(){
		return sections.isEmpty();
	}

	private Station findUpStation() {
		Station downStation = pickCandidateDownStation();
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

	private Station pickCandidateDownStation() {
		Section firstSection = sections.get(0);
		return firstSection.getUpStation();
	}

	public void add(Section section){
		this.sections.add(section);
	}

	public List<Section> getSections() {
		return this.sections;
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		List<Station> stations = getOrderedStations();
		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
			stations.stream().noneMatch(it -> it == downStation)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			add(new Section(line, upStation, downStation, distance));
			return;
		}

		if (isUpStationExisted) {
			sections.stream()
				.filter(it -> it.getUpStation() == upStation)
				.findFirst()
				.ifPresent(it -> it.updateUpStation(downStation, distance));

			add(new Section(line, upStation, downStation, distance));
		} else if (isDownStationExisted) {
			sections.stream()
				.filter(it -> it.getDownStation() == downStation)
				.findFirst()
				.ifPresent(it -> it.updateDownStation(upStation, distance));

			add(new Section(line, upStation, downStation, distance));
		} else {
			throw new RuntimeException();
		}
	}

	public void removeStation(Line line, Station station) {
		if (sections.size() <= 1) {
			throw new RuntimeException();
		}
		Optional<Section> upLineStation = sections.stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst();
		Optional<Section> downLineStation = sections.stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> sections.remove(it));
		downLineStation.ifPresent(it -> sections.remove(it));
	}
}
