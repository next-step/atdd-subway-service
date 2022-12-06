package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static nextstep.subway.common.domain.BizExceptionMessages.*;
import static nextstep.subway.common.domain.BizMagicNumber.SURCHARGE_MIN_BOUNDARY;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String color;
    @Column(nullable = false)
    private int Fare = 0;
    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int Fare) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
        setFare(Fare);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
        addSection(upStation, downStation, distance);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int Fare) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
        setFare(Fare);

        addSection(upStation, downStation, distance);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(this, upStation, downStation, distance);
    }

    public List<Section> getSections() {
        return sections.values();
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public void removeSection(Station station) {
        sections.remove(station);
    }

    public void setFare(int fare) {
        if (fare < SURCHARGE_MIN_BOUNDARY.number()) {
            throw new IllegalArgumentException(CHARGE_IS_NOT_NEGATIVE.message());
        }
        this.Fare = fare;
    }

    public List<Station> getStations() {
        return sections.getStations();
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

    public int getFare() {
        return Fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(getName(), line.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    private void validateLine(String name, String color) {
        if (Objects.isNull(name) || StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(LINE_NAME_INVALID.message());
        }
        if (Objects.isNull(color) || StringUtils.isEmpty(color)) {
            throw new IllegalArgumentException(LINE_COLOR_INVALID.message());
        }
    }
}
