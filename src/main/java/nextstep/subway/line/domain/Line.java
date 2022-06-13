package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    private static final int MIN_SIZE = 1;
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
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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
        return this.sections;
    }

    public void addSection(final Section section) {
        if (!Objects.equals(this, section.getLine())) {
            section.updateLineBy(this);
        }
        if (!sections.isContains(section)) {
            sections.addSection(section);
        }
    }

    public void removeSection(final Station station) {
        if (this.sections.isSize() <= MIN_SIZE) {
            throw new IllegalStateException("구간이 한개 뿐이거나 없는 경우에 삭제할수 없습니다.");
        }
        removeSectionBy(station);
    }

    private void removeSectionBy(Station station) {
        final Optional<Section> upLineStation =
                sections.getSections().stream()
                        .filter(it -> it.isMatchUpStation(station))
                        .findFirst();

        final Optional<Section> downLineStation =
                sections.getSections().stream()
                        .filter(it -> it.isMatchDownStation(station))
                        .findFirst();

        upLineStation.ifPresent(this::removeSection);
        downLineStation.ifPresent(this::removeSection);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.addSection(new Section(this, newUpStation, newDownStation, newDistance));
        }
    }

    public void removeSection(final Section section) {
        if (this.sections.isContains(section)) {
            this.sections.removeSection(section);
        }
    }

    public Optional<Station> getStartStation() {
        return sections.getStartStation();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int isSize() {
        return sections.isSize();
    }

    public boolean isContains(final Section section) {
        return this.sections.isContains(section);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
