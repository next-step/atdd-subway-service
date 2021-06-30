package nextstep.subway.line.domain;

import static java.util.Objects.*;

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

    private static final int NO_ADDITIONAL_FARE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private Integer additionalFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(Long id, String name, String color, Integer additionalFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    Line(Long id, String name, String color) {
        this(id, name, color, null);
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(new Section(upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        section.setLine(this);
        sections.addSection(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public Integer additionalFare() {
        if (hasAdditionalFare()) {
            return additionalFare;
        }

        return NO_ADDITIONAL_FARE;
    }

    private boolean hasAdditionalFare() {
        return nonNull(additionalFare);
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
        return sections.getStationsInOrder();
    }

    public Sections getSections() {
        return sections;
    }
}
