package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.common.BaseEntity;
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
    private Fare additionalFare = new Fare();

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, int additionFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = Fare.valueOf(additionFare);
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        Section.create(this, upStation, downStation, Distance.valueOf(distance));
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = Fare.valueOf(additionalFare);
        Section.create(this, upStation, downStation, Distance.valueOf(distance));
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, int additionalFare) {
        return new Line(name, color, additionalFare);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance, int additionalFare) {
        return new Line(name, color, upStation, downStation, distance, additionalFare);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.additionalFare = line.getAdditionalFare();
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

    public Fare getAdditionalFare() {
        return additionalFare;
    }

    public List<Station> getStations() {
        return this.sections.getSortedStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void add(Section section) {
        sections.add(section);

        if (!section.equalsLine(this)) {
            section.setLine(this);
        }
    }

    public void addLineStation(Section section) {
        if (sections.isEmptyStation()) {
            add(section);
            return;
        }

        sections.validateForAdded(section);
        sections.updateOriginSectionByAdded(section);
        add(section);
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station);
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
