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

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, name = "name")
	private String name;

	@Column(name = "color")
	private String color;

	@Embedded
	private Sections sections = new Sections();

	private Line() {
	}

	private Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public static Line from() {
		return new Line();
	}

	public static Line of(String name, String color) {
		return new Line(name, color);
	}

	public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
		Line line = new Line(name, color);
		line.addSection(Section.of(line, upStation, downStation, distance));
		return line;
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
		return sections.getSectionList();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public void addSection(Section section) {
		sections.addSection(section);
	}

	public void removeStation(Station station) {
		sections.removeStation(station);
	}
}
