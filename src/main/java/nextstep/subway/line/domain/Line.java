package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;
	private int overFare;

	@Embedded
	private final Sections sections = new Sections();

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance, int overFare) {
		this.name = name;
		this.color = color;
		this.overFare = overFare;
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

	public void addSection(Section section) {
		sections.add(section);
	}

	public void removeSection(Station station) {
		sections.remove(station);
	}

	public List<Section> getSections() {
		return sections.values();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public int getOverFare() {
		return overFare;
	}

	public boolean anyContainsSection(List<Stations> stations){
		return stations.stream()
			.anyMatch(it -> sections.contains(it.getStations()));
	}
}
