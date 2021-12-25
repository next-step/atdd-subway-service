package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
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
	private Fare fare;

	@Embedded
	private Sections sections = Sections.of();

	protected Line() {
	}

	private Line(Long id, String name, String color, int fare) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.fare = Fare.of(fare);
	}

	public static Line of(String name, String color, Station upStation, Station downStation, int distance, int fare) {
		Line line = new Line(null, name, color, fare);
		line.sections.add(Section.of(line, upStation, downStation, Distance.of(distance)));
		return line;
	}

	public static Line of(Long id, String name, String color, Station upStation, Station downStation, int distance,
		int fare) {
		Line line = new Line(id, name, color, fare);
		Section section = Section.of(line, upStation, downStation, Distance.of(distance));
		line.addSection(section);
		return line;
	}

	public static Line of(String name, String color, int fare) {
		return new Line(null, name, color, fare);
	}

	public static Line of(Long id, String name, String color, int fare) {
		return new Line(id, name, color, fare);
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}

	public void addStation(Section newSection) {
		this.sections.addStation(newSection);
	}

	public void removeStation(Station station) {
		this.sections.removeStation(station, this);
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	public boolean containsAnySection(List<Station> stationList) {
		return this.sections.containsAnySection(stationList);
	}

	public int compareByFare(Line other) {
		return this.fare.compareTo(other.fare);
	}

	public List<Station> getStations() {
		return this.sections.getStations();
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
		return this.sections;
	}

	public Fare getFare() {
		return this.fare;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Line line = (Line)o;

		return id.equals(line.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
