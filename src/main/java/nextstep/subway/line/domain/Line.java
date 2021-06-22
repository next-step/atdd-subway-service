package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

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

    public Line() {
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
        return sections;
    }

    public List<Station> getStations() {
        return sections.stations();
    }

    public void addSection(Section section) {
        if (sections.contains(section)) {
            return;
        }

        if (sections.isEmpty()) {
            add(section);
            return;
        }

        validateAddable(section);
        updateForConnection(section);
    }

    private void add(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    private void updateForConnection(Section section) {
        List<Station> stations = this.getStations();

        if (section.upStationIsIn(stations)) {
            sections.updateIfMidFront(section);
            add(section);
            return;
        }

        if (section.downStationIsIn(stations)) {
            sections.updateIfMidRear(section);
            add(section);
            return;
        }

        throw new RuntimeException();
    }

    private void validateAddable(Section section) {
        checkExistedStationsOf(section);
        hasStationCanBeConnectedIn(section);
    }

    private void checkExistedStationsOf(Section section) {
        if (sections.checkExistedStationsOf(section)) {
            throw new RuntimeException("이미 등록된 역들을 가진 구간 입니다.");
        }
    }

    private void hasStationCanBeConnectedIn(Section section) {
        if (!sections.hasStationCanBeConnectedIn(section)) {
            throw new RuntimeException("연결 가능한 역이 하나도 없는 구간 입니다.");
        }
    }

    public void remove(Station station) {
        validateRemovable(station);
        sections.remove(station);
    }

    private void validateRemovable(Station station) {
        if (!getStations().contains(station)) {
            throw new RuntimeException();
        }
        sections.validateRemovableSize();
    }
}
