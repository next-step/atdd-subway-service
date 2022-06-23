package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NoSearchStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private LineName name;
    @Embedded
    private LineColor color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void deleteSection(Station station) throws NoSearchStationException {
        sections.delete(station);
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
