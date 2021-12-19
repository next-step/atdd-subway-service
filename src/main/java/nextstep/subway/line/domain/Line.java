package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.path.domain.SubwayFare;
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

    @Embedded
    private Sections sections;

    @Embedded
    private SubwayFare overFare;

    public Line() {
        this(null,null, null, null, null, null);
    }

    public Line(String name, String color) {
        this(name,color, null, null, null, null);
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, SubwayFare overFare) {
        this.name = name;
        this.color = color;
        this.overFare = overFare;
        sections = new Sections(new Section(this, upStation, downStation, distance));
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
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

	public void remove(Station station) {
        sections.remove(this, station);
	}

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getOverFare() {
        return overFare.value();
    }
}
