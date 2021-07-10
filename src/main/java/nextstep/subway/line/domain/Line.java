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
	private int extraFare;  // 추가요금

	@Embedded
	private Sections sections = new Sections();

	public Line() {
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this(name, color, 0, upStation, downStation, distance);
	}

	public Line(String name, String color, int extraFare, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		this.extraFare = extraFare;
		addSection(upStation, downStation, distance);
	}

	public void update(String name, String color) {
		this.name = name;
		this.color = color;
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

	public int getExtraFare() {
		return extraFare;
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public void addSection(Station upStation, Station downStation, int distance) {
		Section section = new Section(this, upStation, downStation, distance);
		sections.addSection(section);
	}

	public void deleteSection(Station station) {
		sections.deleteSection(station);
	}

	public Sections getSections() {
		return sections;
	}
}

