package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
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
	private Sections sections;

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
		this.sections = new Sections();
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		this.sections = new Sections(new Section(this, upStation, downStation, distance));
	}

	public static Line of(LineRequest request, Station upStation, Station downStation) {
		return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
	}

	public static Line of(LineRequest lineUpdateRequest) {
		return new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
	}

	public void update(Line line) {
		this.name = line.name();
		this.color = line.color();
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String color() {
		return color;
	}

	public List<Section> sections() {
		return sections.value();
	}

	public List<Station> stations() {
		return this.sections.stations();
	}

	public void addLineStation(Section section) {
		this.sections.addLineStation(section);
	}

	public void removeLineStation(Station station) {
		this.sections.removeLineStation(this, station);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Line)) {
			return false;
		}
		Line line = (Line)object;
		return Objects.equals(id, line.id)
			&& Objects.equals(name, line.name)
			&& Objects.equals(color, line.color)
			&& sections.equals(line.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, color, sections);
	}
}
