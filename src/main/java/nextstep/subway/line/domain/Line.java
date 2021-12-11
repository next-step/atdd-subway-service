package nextstep.subway.line.domain;

import java.util.Collections;
import java.util.Objects;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.domain.fare.Money;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String color;

    @Embedded
    private final Sections sections = Sections.of();

    @Column
    private int additionalFare;

    public Line() {
    }

    public Line(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance,
        int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        addSection(upStation, downStation, distance);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        Section section = Section.of(this, upStation, downStation, distance);
        this.sections.add(section);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(sections.getStationsInOrder());
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

    public boolean isSameNameAndColor(Line line) {
        return Objects.equals(this.color, line.color)
            && Objects.equals(this.name, line.name);
    }

    public void removeStation(Station station) {
        sections.remove(station);
    }


    public List<Section> getSections() {
        return Collections.unmodifiableList(sections.getSections());
    }

    public Money sumAdditionalFare(Money money) {
        return money.plus(Money.wons(additionalFare));
    }
}
