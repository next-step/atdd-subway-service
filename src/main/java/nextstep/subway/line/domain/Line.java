package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String color;
    @Embedded
    private final Sections sections = Sections.instance();
    @Embedded
    private ExtraCharge extraCharge;

    protected Line() {}

    public Line(final String name, final String color, final Station upStation,
                final Station downStation, final int distance, final int extraCharge) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.extraCharge = ExtraCharge.of(extraCharge);
    }

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this(name, color, upStation, downStation, distance, 0);
    }

    public Line(final String name, final String color) {
      this.name = name;
      this.color = color;
    }

    public void update(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(final Section section) {
        sections.add(section);
    }

    public void deleteStation(final Station station) {
        sections.delete(station);
    }

    public List<Station> getSortedStations() {
        return sections.getSortedStations();
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getExtraCharge() {
        return extraCharge.getValue();
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                ", charge=" + extraCharge +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(sections, line.sections) && Objects.equals(extraCharge, line.extraCharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections, extraCharge);
    }
}
