package nextstep.subway.line.domain;

import java.math.BigDecimal;
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

	private BigDecimal extraPrice;

	@Embedded
	private Sections sections = new Sections();

	protected Line() {
	}

	private Line(String name, String color) {
		this.name = name;
		this.color = color;
		this.extraPrice = BigDecimal.ZERO;
	}

	private Line(String name, String color, BigDecimal extraPrice) {
		this.name = name;
		this.color = color;
		this.extraPrice = extraPrice == null ? BigDecimal.ZERO : extraPrice;
	}

	public static Line from() {
		return new Line();
	}

	public static Line of(String name, String color) {
		return new Line(name, color);
	}

	public static Line of(String name, String color, int extraPrice) {
		return new Line(name, color, new BigDecimal(extraPrice));
	}

	public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
		Line line = new Line(name, color);
		line.addSection(Section.of(line, upStation, downStation, distance));
		return line;
	}

	public static Line of(String name, String color, Station upStation, Station downStation, int distance,
		int extraPrice) {
		Line line = new Line(name, color, new BigDecimal(extraPrice));
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

	public BigDecimal getExtraPrice() {
		return extraPrice;
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
