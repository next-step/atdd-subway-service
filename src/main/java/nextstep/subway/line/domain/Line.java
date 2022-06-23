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

    private int surcharge;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public Line(String name, String color, int surcharge, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
        sections.addSection(this, upStation, downStation, new Distance(distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.surcharge = line.getSurcharge();
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

    public int getSurcharge() {
        return surcharge;
    }

    public List<Section> getSections() {
        return sections.getList();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sections.addSection(this, upStation, downStation, new Distance(distance));
    }

    public void removeSection(final Station station) {
        sections.removeSection(this, station);
    }
}
