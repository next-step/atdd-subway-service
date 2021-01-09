package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;
	private int additionalFare;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
		  CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		sections.add(new Section(this, upStation, downStation, distance));
	}

	public Line(String name, String color, int additionalFare) {
		this.name = name;
		this.color = color;
		this.additionalFare = additionalFare;
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	public void updateUpStation(Station upStation, Station downStation, int distance) {
		this.sections.stream()
			  .filter(it -> it.getUpStation() == upStation)
			  .findFirst()
			  .ifPresent(it -> it.updateUpStation(downStation, distance));

		this.sections.add(new Section(this, upStation, downStation, distance));
	}

	public void updateDownStation(Station upStation, Station downStation, int distance) {
		this.sections.stream()
			  .filter(it -> it.getDownStation() == downStation)
			  .findFirst()
			  .ifPresent(it -> it.updateDownStation(upStation, distance));

		this.sections.add(new Section(this, upStation, downStation, distance));
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
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
		Station downStation = this.sections.get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.sections.stream()
				  .filter(it -> it.getDownStation() == finalDownStation)
				  .findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}

	public Optional<Section> upLineStation(Station station) {
		return this.sections.stream()
			  .filter(it -> it.isUpStationEquals(station))
			  .findFirst();
	}

	public Optional<Section> downLineStation(Station station) {
		return this.sections.stream()
			  .filter(it -> it.isDownStationEquals(station))
			  .findFirst();
	}

	public void addSection(Station newUpStation, Station newDownStation, int newDistance) {
		this.sections.add(new Section(this, newUpStation, newDownStation, newDistance));
	}

	public void removeSection(Section section) {
		this.sections.remove(section);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public int getAdditionalFare() {
		return additionalFare;
	}

	public List<Section> getSections() {
		return sections;
	}

	public boolean emptyOrHasOneSection() {
		return this.sections.isEmpty() || this.sections.size() == 1;
	}
}
