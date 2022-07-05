package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.sections.domain.Sections;
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
    @Embedded
    private Fare fare = Fare.of(0);

    public Line() {
        sections = new Sections();
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        section.setLine(this);
        sections = new Sections(section);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections = new Sections(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void changeFare(long fare) {
        this.fare = Fare.of(fare);
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

    public List<Station> orderedStations() {
        return sections.orderedStations();
    }

    public void updateSection(Station upStation, Station downStation, int distance) {
        Section section = new Section(upStation, downStation, distance);
        section.setLine(this);
        sections.updateSection(section);
    }

    public void removeSectionByStation(Station station) {
        sections.delete(station);
    }

    public Fare getFare() {
        return fare;
    }
}
