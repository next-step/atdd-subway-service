package nextstep.subway.line.domain;

import java.util.ArrayList;
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

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.addSection(Section.of(this, upStation, downStation, distance));
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
        return sections.getSections();
    }

    public void addSection(final Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = isStationExists(section.getUpStation());
        boolean isDownStationExisted = isStationExists(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && isStationNotExists(section.getUpStation()) &&
            isStationNotExists(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.addSection(section);
            return;
        }

        if (isUpStationExisted) {
            getUpStationMatchSection(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.addSection(section);
        } else if (isDownStationExisted) {
            getDownStationMathSection(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.addSection(section);
        } else {
            throw new RuntimeException();
        }
    }

    public List<Station> getStations() {
        if (getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = getUpStationMatchSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public Station findUpStation() {
        Station downStation = getSections().get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = getDownStationMathSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public Optional<Section> getUpStationMatchSection(final Station upStation) {
        return getSections().stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst();
    }

    public Optional<Section> getDownStationMathSection(final Station downStation) {
        return getSections().stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst();
    }

    public boolean isStationNotExists(final Station station) {
        return getStations().stream().noneMatch(it -> it == station);
    }

    public boolean isStationExists(final Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    public void removeStation(final Station station) {
        if (getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = getUpStationMatchSection(station);
        Optional<Section> downLineStation = getDownStationMathSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().add(downLineStation.get().getDistance());
            sections.addSection(Section.of(this, newUpStation, newDownStation, newDistance.getDistance()));
        }

        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }
}
