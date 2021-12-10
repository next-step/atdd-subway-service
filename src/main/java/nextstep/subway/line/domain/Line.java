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
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private Long surcharge;

    @Embedded
    Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.surcharge = 0L;
    }

    public Line(String name, String color, Long surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color);
        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public Line(final long id, String name, String color, Long surcharge) {
        this(name, color, surcharge);
        this.id = id;
    }

    public Line() {
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void addLineStation(Section section) {
        this.sections.addLineStation(section);
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station);
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

    public Long getSurcharge() {
        return surcharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
