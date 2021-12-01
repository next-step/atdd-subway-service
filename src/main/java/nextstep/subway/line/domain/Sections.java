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

	public void add(Section section) {
		values.add(section);
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
