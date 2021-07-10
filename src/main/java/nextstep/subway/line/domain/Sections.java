package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	private static final int MINIMUM_SECTION_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}

		return makeOrderedStations();
	}

	private List<Station> makeOrderedStations() {
		List<Station> stations = new ArrayList<>();
		Station downStation = findStartStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = findCommonUpStationSection(finalDownStation);
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = getNextStation(nextLineStation);
			stations.add(downStation);
		}

		return stations;
	}

	private Station getNextStation(Optional<Section> nextLineStation) {
		return nextLineStation.get().getDownStation();
	}

	private Station findStartStation() {
		Station downStation = getAnyStation();

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = findCommonDownStationSection(finalDownStation);
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	private Station getAnyStation() {
		return sections.stream().findAny().get().getUpStation();
	}

	public void addLineStation(Section section) {
		if (sections.isEmpty()) {
			sections.add(section);
			return;
		}

		List<Station> stations = getStations();
		validateTwoStationsAlreadyExists(stations, section);
		validateConnectedStationToOldLineExists(section, stations);

		if (isStationExisted(section.getUpStation(), stations)) {
			modifyCommonUpStationSection(section);
			sections.add(section);

			return;
		}

		if (isStationExisted(section.getDownStation(), stations)) {
			modifyCommonDownStationSection(section);
			sections.add(section);

			return;
		}

		throw new RuntimeException();
	}

	private void modifyCommonDownStationSection(Section section) {
		findCommonDownStationSection(section.getDownStation())
			.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
	}

	private void modifyCommonUpStationSection(Section section) {
		findCommonUpStationSection(section.getUpStation())
			.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
	}

	private boolean isStationExisted(Station station, List<Station> stations) {
		return stations.stream().anyMatch(it -> it.equals(station));
	}

	private void validateConnectedStationToOldLineExists(Section section, List<Station> stations) {
		if (isConnectedStaionToOldLineExisted(section, stations)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private boolean isConnectedStaionToOldLineExisted(Section section, List<Station> stations) {
		return !stations.isEmpty() && isMatchExisted(section.getUpStation(), stations) && isMatchExisted(section.getDownStation(), stations);
	}

	private boolean isMatchExisted(Station station, List<Station> stations) {
		return stations.stream().noneMatch(it -> it.equals(station));
	}

	private void validateTwoStationsAlreadyExists(List<Station> stations, Section section) {
		if(isTwoStationAlreadyExisted(stations, section)){
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	private boolean isTwoStationAlreadyExisted(List<Station> stations, Section section) {
		return stations.stream()
			.filter(station -> station.equals(section.getUpStation()) || station.equals(section.getDownStation()))
			.distinct()
			.count() == 2;
	}

	public void removeLineStation(Line line, Station station) {
		validateStationRemovableInSections();

		Optional<Section> upLineStation = findCommonUpStationSection(station);
		Optional<Section> downLineStation = findCommonDownStationSection(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			connectTwoStation(line, upLineStation, downLineStation);
		}

		remove(upLineStation);
		remove(downLineStation);
	}

	private void remove(Optional<Section> optionalSection){
		optionalSection.ifPresent(it -> sections.remove(it));
	}

	private void connectTwoStation(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
		Station newUpStation = downLineStation.get().getUpStation();
		Station newDownStation = upLineStation.get().getDownStation();
		int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();

		sections.add(new Section(line, newUpStation, newDownStation, newDistance));
	}

	private Optional<Section> findCommonDownStationSection(Station station) {
		return sections.stream()
			.filter(it -> it.getDownStation().equals(station))
			.findFirst();
	}

	private Optional<Section> findCommonUpStationSection(Station station) {
		return sections.stream()
			.filter(it -> it.getUpStation().equals(station))
			.findFirst();
	}

	private void validateStationRemovableInSections() {
		if (sections.size() <= MINIMUM_SECTION_SIZE) {
			throw new RuntimeException();
		}
	}

	public void setLengthBetweenTwoStation(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
		for (Section section : sections) {
			section.setLengthBetweenTwoStation(graph);
		}
	}
}
