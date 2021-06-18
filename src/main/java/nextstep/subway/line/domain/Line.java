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

    public List<Section> getSections() {
        return sections.get();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = this.findTopMostStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    protected Station findTopMostStation() {
        Optional<Section> topSection = sections.stream()
                .filter(section -> isTop(section))
                .findFirst();
        return topSection.get().getUpStation();
    }

    private boolean isTop(Section section) {
        return sections.stream()
                .noneMatch(it -> it.getDownStation().equals(section.getUpStation()));
    }

    public void addSection(Section section) {
        List<Station> stations = this.getStations();
        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        validate(section);
        updateForConnection(section);
    }

    private void updateForConnection(Section section) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        if (isUpStationExisted) {
            sections.updateIfMidFront(section);
            sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            sections.updateIfMidRear(section);
            sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    private void validate(Section section) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void remove(Station station) {
        if (this.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upSection = sections.hasSameUpStationWith(station);
        Optional<Section> downSection = sections.hasSameDownStationWith(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
            this.getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(it -> this.getSections().remove(it));
        downSection.ifPresent(it -> this.getSections().remove(it));
    }
}
