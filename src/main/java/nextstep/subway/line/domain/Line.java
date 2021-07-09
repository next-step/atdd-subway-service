package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.InvalidLineException;
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
	private Sections sections = new Sections();

	protected Line() {
	}

	public Line(String name, String color, Fare fare) {
		validateLine(name, color);
		this.name = name;
		this.color = color;
		this.fare = fare;
	}

	public Line(Long id, String name, String color, Fare fare) {
		this(name, color, fare);
		this.id = id;
	}

	public void update(Line line) {
		validateNullLine(line);
		validateLine(line.name, line.color);
		this.name = line.name;
		this.color = line.color;
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

	public Fare getFare() {
		return fare;
	}

	public List<Station> getStations() {
		return this.sections.getStations();
	}

	public Sections getSections() {
		return this.sections;
	}

	public void addLineStation(Section section) {
		this.sections.addSection(section);
	}

	public void removeLineStation(Station station) {
		this.sections.removeStation(this, station);
	}

	public int minusFare(Line otherLine) {
		return this.fare.value() - otherLine.fare.value();
	}

	private void validateLine(String name, String color) {
		if (name.isEmpty() || color.isEmpty()) {
			throw new InvalidLineException("노선의 이름이나 색깔이 비워져있을 수 없습니다.");
		}
	}

	private void validateNullLine(Line line) {
		if (!Objects.nonNull(line)) {
			throw new InvalidLineException("노선이 null일 수 없습니다.");
		}
	}
}
