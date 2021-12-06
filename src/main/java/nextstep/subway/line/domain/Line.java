package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.NotFoundException;
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

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        Section.create(this, upStation, downStation, Distance.valueOf(distance));
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
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

    public List<Station> getStations() {
        return this.sections.getSortedStations();
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void addSection(Section section) {
        if (!hasSection(section)) {
            sections.add(section);
        }

        if (!section.equalsLine(this)) {
            section.toLine(this);
        }
    }

    public void addLineStation(Section section) {
        if (sections.isEmptyStation()) {
            addSection(section);
            return;
        }
        validateForAdded(section);

        sections.updateSection(section);
        addSection(section);
    }

    public void removeLineStation(Station station) {

        Optional<Section> upLineStation = sections.findByUpStation(station);
        Optional<Section> downLineStation = sections.findByDownStation(station);

        validateForRemove(upLineStation, downLineStation);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addLineStationByRemove(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateForAdded(Section section) {
        if (sections.isAlreadySection(section)) {
            throw new CannotAddException("이미 등록된 구간 입니다.");
        }

        if (!sections.isIncludeStationOfSection(section)) {
            throw new CannotAddException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateForRemove(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new NotFoundException("역이 포함된 구간이 없습니다.");
        }

        if (sections.isMinSize()) {
            throw new CannotDeleteException("구간이 하나는 존재해야 합니다.");
        }

    }

    private void addLineStationByRemove(Section upLineStation, Section downLineStation) {
        addSection(
            Section.from(
                this, downLineStation, upLineStation,
                upLineStation.plusDistance(downLineStation)));
    }

    private boolean hasSection(Section section) {
        return sections.contains(section);
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
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
