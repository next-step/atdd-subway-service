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

    @Embedded
    private Sections sections = new Sections();

    private BigDecimal surcharge;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, BigDecimal surcharge) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.surcharge = surcharge;
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, BigDecimal surcharge) {
        return new Line(name, color, upStation, downStation, distance, surcharge);
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
        return sections.getStations();
    }

    public Station findUpStation() {
        return sections.findUpStation();
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void removeLineStation(Station station) {
        this.sections.removeLineStation(station);
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }
}
