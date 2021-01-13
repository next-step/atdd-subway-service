package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.fare.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    private static final int FREE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Fare additionalFare;

    @Embedded
    private final Sections sections = Sections.create();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.additionalFare = Fare.of(FREE);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.additionalFare = Fare.of(FREE);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeSection(final Station station) {
        sections.remove(station, this);
    }

    public void changeAdditionalFare(final int fare) {
        this.additionalFare = Fare.of(fare);
    }

    public List<Section> getSections() {
        return sections.getSections();
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

    public int getAdditionalFare() {
        if (additionalFare == null) {
            return FREE;
        }

        return this.additionalFare.getFare();
    }
}
