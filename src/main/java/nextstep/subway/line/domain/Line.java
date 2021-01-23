package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
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

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
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

	public List<Section> getSections() {
		return sections;
	}

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		Station station = findFirstUpStation();

		while (Objects.nonNull(station)) {
			stations.add(station);
			station = findDownStationByUpStation(station);
		}

		return stations;
	}

	private Station findFirstUpStation() {
		Station upStation = null;
		Station station = sections.get(0).getUpStation();

		while (Objects.nonNull(station)) {
			upStation = station;
			station = findUpStationByDownStation(upStation);
		}

		return upStation;
	}

	private Station findUpStationByDownStation(Station downStation) {
		return sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst()
			.map(Section::getUpStation)
			.orElse(null);
	}

	private Station findDownStationByUpStation(Station upStation) {
		return sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst()
			.map(Section::getDownStation)
			.orElse(null);
	}
}
