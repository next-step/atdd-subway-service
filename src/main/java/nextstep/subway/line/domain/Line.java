package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.Optional;
import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
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
    private ExtraCharge extraCharge;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, int extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = ExtraCharge.of(extraCharge);
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this(name, color, 0);
        sections.initSection(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, int extraCharge, Station upStation, Station downStation, Distance distance) {
        this(name, color, extraCharge);
        sections.initSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public Sections getSections() {
        return this.sections;
    }

    public ExtraCharge getExtraCharge() {
        return this.extraCharge;
    }

    public void addSection(Section section) {
        if (this.sections.isEmpty()) {
            this.sections.initSection(section);
            return;
        }
        this.sections.addSection(section);
    }

    public void deleteSection(Station station) {
        this.sections.deleteSection(station);
    }

    public LineResponse toLineResponse() {
        StationsResponse stationsResponse = StationsResponse.of(this.sections);
        return new LineResponse(this.id, this.name, this.color, stationsResponse.getStations(), this.getCreatedDate(),
                this.getModifiedDate());
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
