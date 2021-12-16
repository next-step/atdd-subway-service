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

    public static final int DEFAULT_ADDITIONAL_FARE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    public Sections sections = new Sections();

    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this(name, color, upStation, downStation, distance, DEFAULT_ADDITIONAL_FARE);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance,
        int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;

        sections.addSection(Section.of(this, upStation, downStation, distance));
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

    public void addSection(final Section section) {
        sections.addSection(section);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
