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
import nextstep.subway.path.domain.Fare;
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
    private Sections sections = new Sections();

    @Embedded
    private Fare surcharge = Fare.of();

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(
        final String name,
        final String color,
        final Station upStation,
        final Station downStation,
        final int distance
    ) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(
        final String name,
        final String color,
        final Station upStation,
        final Station downStation,
        final int distance,
        final Fare surcharge
    ) {
        this(name, color, upStation, downStation, distance);
        this.surcharge = surcharge;
    }

    public void changeName(final String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public boolean nameEquals(final String name) {
        return this.name.equals(name);
    }

    public void changeColor(final String color) {
        if (Objects.isNull(color) || color.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.color = color;
    }

    public void addSection(final Section section) {
        section.attachToLine(this);
        sections.add(section);
    }

    public void deleteSection(final Station station) {
        sections.deleteStation(station);
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

    public List<Station> getStationsInOrder() {
        return sections.getStationsInOrder();
    }

    public Fare getSurcharge() {
        return surcharge;
    }
}
