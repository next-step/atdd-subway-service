package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
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
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        return this.sections.getStations().get();
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public boolean hasSection(Section section) {
        return sections.contains(section);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        Stations stations = this.sections.getStations();

        if (stations.isEmpty()) {
            Section.create(this, upStation, downStation, Distance.valueOf(distance));
            return;
        }

        validateForAdded(upStation, downStation);

        sections.updateSection(upStation, downStation, distance);
        addSection(Section.create(this, upStation, downStation, Distance.valueOf(distance)));
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

    private void validateForAdded(Station upStation, Station downStation) {
        Stations stations = this.sections.getStations();
        if (stations.anyMatch(upStation) && stations.anyMatch(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.noneMatch(upStation) && stations
            .noneMatch(downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateForRemove(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new RuntimeException("역이 포함된 구간이 없습니다.");
        }

        if (sections.isMinSize()) {
            throw new RuntimeException("구간이 하나는 존재해야 합니다.");
        }

    }

    private void addLineStationByRemove(Section upLineStation, Section downLineStation) {
        addSection(
            Section.from(
                this, downLineStation, upLineStation,
                upLineStation.plusDistance(downLineStation)));
    }
}
