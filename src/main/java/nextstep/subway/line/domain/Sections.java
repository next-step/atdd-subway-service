package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
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
			Section section = findByUpStation(upStation);
			if (section == null) {
				break;
			}
			upStation = section.getDownStation();
		}
	}

	private Section findByUpStation(Station upStation) {
		return values.stream()
			.filter((value) -> upStation.equals(value.getUpStation()))
			.findFirst()
			.orElse(null);
	}

	public int size() {
		return values.size();
	}

	public List<Section> getValues() {
		return values;
	}
}
