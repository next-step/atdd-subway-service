package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;
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
    @Embedded
    private LineName name;
    @Embedded
    private LineColor color;
    @Embedded
    private Sections sections = Sections.createEmpty();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
    }

    private Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.sections.add(Section.of(this, upStation, downStation, distance));
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public static Line of(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line createEmpty() {
        return new Line();
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return this.name;
    }

    public LineColor getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void addSection(Section section) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
            stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            this.sections.add(section);
            section.registerLine(this);
            return;
        }

        if (isUpStationExisted) {
            this.getSections().stream()
                .filter(it -> it.getUpStation().equals(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            this.sections.add(section);
            section.registerLine(this);
        } else if (isDownStationExisted) {
            this.getSections().stream()
                .filter(it -> it.getDownStation().equals(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            this.sections.add(section);
            section.registerLine(this);
        } else {
            throw new RuntimeException();
        }
    }

    public void removeSection(Station station) {
        if (this.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = this.getSections().stream()
            .filter(it -> it.getUpStation().equals(station))
            .findFirst();
        Optional<Section> downLineStation = this.getSections().stream()
            .filter(it -> it.getDownStation().equals(station))
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistanceValue() + downLineStation.get().getDistanceValue();

            this.sections.add(Section.of(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }
}
