package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = values.stream()
				.filter(it -> it.getUpStation().equals(finalDownStation))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return Stations.of(stations);
	}

	private Station findUpStation() {
		Station downStation = getFirst().getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = values.stream()
				.filter(it -> it.getDownStation().equals(finalDownStation))
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	public Section getFirst() {
		return values.get(0);
	}

	public int size() {
		return values.size();
	}

	public List<Section> getValues() {
		return values;
	}
}
