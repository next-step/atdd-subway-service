package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

import static nextstep.subway.line.domain.BizExceptionMessages.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        validateLine(name, color);
        this.name = name;
        this.color = color;
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
        sections.remove(this, station);
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
