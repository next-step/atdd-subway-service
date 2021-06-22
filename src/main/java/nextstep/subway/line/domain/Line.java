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
	@Column(unique = true)
	private String name;
	private String color;
	@Embedded
	private Sections sections = new Sections();

	protected Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Line(Long id, String name, String color) {
		this(name, color);
		this.id = id;
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

	public List<Station> getStations() {
		return this.sections.getStations();
	}

	public void addLineStation(Section section) {
		this.sections.addSection(section);
	}

	public void removeLineStation(Station station) {
		this.sections.removeStation(this, station);
	}
}
