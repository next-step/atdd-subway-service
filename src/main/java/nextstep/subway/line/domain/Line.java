package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import nextstep.subway.station.dto.StationsResponse;

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
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public Optional<Section> findUpSection(Station station) {
        return this.sections.findUpSection(station);
    }

    public Optional<Section> findDownSection(Station station) {
        return this.sections.findDownSection(station);
    }

    public LineResponse toLineResponse(StationsResponse stations) {
        return new LineResponse(this.id, this.name, this.color, stations.getStations(), this.getCreatedDate(), this.getModifiedDate());
    }

    public Sections getSections() {
        return this.sections;
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name)
                && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
