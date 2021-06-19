package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public Line(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
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
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validate(section);
        updateForConnection(section);
    }

    private void updateForConnection(Section section) {
        List<Station> stations = this.getStations();

        if (section.upStationIsIn(stations)) {
            sections.updateIfMidFront(section);
            sections.add(section);
            return;
        }

        if (section.downStationIsIn(stations)) {
            sections.updateIfMidRear(section);
            sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    private void validate(Section section) {
        validateBothEnrolled(section);
        validateBothNotExist(section);
    }

    private void validateBothEnrolled(Section section) {
        if (sections.alreadyHas(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateBothNotExist(Section section) {
        if (sections.cannotConnect(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
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
