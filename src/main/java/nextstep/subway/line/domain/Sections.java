package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
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

	// TODO : delete this
	public void add(Section section) {
		values.add(section);
	}

	// TODO : refactor this
	public void toBeAdd(Section section) {
		Stations stations = getStations();

		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();

		boolean isUpStationExisted = stations.anyMatch(upStation);
		boolean isDownStationExisted = stations.anyMatch(downStation);

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.noneMatch(upStation) && stations.noneMatch(downStation)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			values.add(section);
			return;
		}

		if (isUpStationExisted) {
			findByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));
			values.add(section);
		} else if (isDownStationExisted) {
			findByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));
			values.add(section);
		} else {
			throw new RuntimeException();
		}
	}

	protected Sections() {

	}

	private Sections(List<Section> sections) {
		this.values = sections;
	}

	public static Sections of(List<Section> sections) {
		if (sections == null) {
			return new Sections(new ArrayList<>());
		}

		return new Sections(sections);
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
