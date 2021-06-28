package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.SortedStations;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	private int extraCharge;

	@Embedded
	private Sections sections = new Sections();

	protected Line() {
	}

	public Line(String name, String color, int extraCharge) {
		this.name = name;
		this.color = color;
		this.extraCharge = extraCharge;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance, int extraCharge) {
		this.name = name;
		this.color = color;
		this.extraCharge = extraCharge;
		sections.addSection(new Section(this, upStation, downStation, distance));
	}

	public int getExtraCharge() {
		return extraCharge;
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

	public Sections getSections() {
		return sections;
	}

	public List<Station> getStations() {
		return this.getSortedStations().getStations();
	}

	private SortedStations getSortedStations() {
		return this.sections.getSortedStations();
	}

	public void addSection(Station upStation, Station downStation, int distance) {
		this.sections.addSection(new Section(this, upStation, downStation, distance));
	}

	public void removeSection(Station station) {
		this.sections.removeSection(station);

	}
}
