package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> values = new ArrayList<>();

	public void add(Section section) {
		throwOnBothStationsAlreadyRegistered(section);
		throwOnBothStationsNotRegistered(section);

		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();

		findByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));
		findByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));

		values.add(section);
	}

	private void throwOnBothStationsAlreadyRegistered(Section section) {
		Stations stations = getStations();
		if (stations.anyMatch(section.getUpStation()) && stations.anyMatch(section.getDownStation())) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	private void throwOnBothStationsNotRegistered(Section section) {
		Stations stations = getStations();
		if (!stations.isEmpty()
			&& stations.noneMatch(section.getUpStation())
			&& stations.noneMatch(section.getDownStation())) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	protected Sections() {

	}

	private Sections(List<Section> sections) {
		this.values = new ArrayList<>(sections);
	}

	public static Sections of(List<Section> sections) {
		if (sections == null) {
			return new Sections(new ArrayList<>());
		}

		return new Sections(sections);
	}

	public static Sections empty() {
		return new Sections(new ArrayList<>());
	}

	public Stations getStations() {
		if (values.isEmpty()) {
			return Stations.empty();
		}

		List<Station> stations = new ArrayList<>();
		Station upStation = findUpStation();
		addStationsInOrder(stations, upStation);

		return Stations.of(stations);
	}

	private Station findUpStation() {
		List<Station> downStations = values.stream()
			.map(Section::getDownStation)
			.collect(Collectors.toList());

		return values.stream()
			.map(Section::getUpStation)
			.filter(upStation -> !downStations.contains(upStation))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
	}

	private void addStationsInOrder(List<Station> stations, Station upStation) {
		while (upStation != null) {
			stations.add(upStation);
			Optional<Section> section = findByUpStation(upStation);
			if (!section.isPresent()) {
				break;
			}
			upStation = section.get().getDownStation();
		}
	}

	public Optional<Section> findByUpStation(Station upStation) {
		return findOne((section) -> upStation.equals(section.getUpStation()));
	}

	public Optional<Section> findByDownStation(Station downStation) {
		return findOne((section) -> downStation.equals(section.getDownStation()));
	}

	private Optional<Section> findOne(Predicate<Section> predicate) {
		return values.stream()
			.filter(predicate)
			.findFirst();
	}

	public List<Integer> getDistances() {
		return getSections().stream()
			.map(Section::getDistance)
			.collect(Collectors.toList());
	}

	private List<Section> getSections() {
		if (values.isEmpty()) {
			return Collections.emptyList();
		}

		List<Section> sections = new ArrayList<>();
		Map<Station, Section> sectionByUpStation = getSectionByUpStation();
		Stations stations = getStations();
		for (int i = 0; i < stations.size() - 1; i++) {
			Station station = stations.get(i);
			sections.add(sectionByUpStation.get(station));
		}

		return sections;
	}

	private Map<Station, Section> getSectionByUpStation() {
		return values.stream()
			.collect(Collectors.toMap(Section::getUpStation, (section) -> section));
	}

	public int size() {
		return values.size();
	}

	public boolean contains(Section section) {
		return values.contains(section);
	}

	public void remove(Section section) {
		values.remove(section);
	}

	public List<Section> getValues() {
		return values;
	}
}
