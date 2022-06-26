package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Embedded
    private final Sections sections = new Sections();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private LineName name;
    @Embedded
    private LineColor color;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        sections.add(new Section.Builder(upStation, downStation, distance).line(this).build());
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public void update(Line line) {
        this.name = LineName.from(line.getName());
        this.color = LineColor.from(line.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return Objects.nonNull(name) ? name.toString() : null;
    }

    public String getColor() {
        return Objects.nonNull(color) ? color.toString() : null;
    }

    public List<Section> getSections() {
        return sections.get();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addLineStation(Section section) {
        sections.add(section);
    }

    public void removeLineStation(Station station) {
        sections.remove(station);
    }
}
