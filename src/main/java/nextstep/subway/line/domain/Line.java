package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;
	private BigDecimal extraCharge;

	@Embedded
	private Sections sections = new Sections();

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
		this.extraCharge = BigDecimal.ZERO;
	}

	public Line(String name, String color, BigDecimal extraCharge) {
		this.name = name;
		this.color = color;
		this.extraCharge = extraCharge;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		this.extraCharge = BigDecimal.ZERO;
		sections.add(new Section(this, upStation, downStation, distance));
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance, BigDecimal extraCharge) {
		this.name = name;
		this.color = color;
		this.extraCharge = extraCharge;
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
		return sections.getSections();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public BigDecimal getExtraCharge() {
		return extraCharge;
	}

	public void addSection(Section section) {
		sections.addSection(section);
	}

	public void removeStation(Station station) {
		sections.removeStation(this, station);
	}
}
