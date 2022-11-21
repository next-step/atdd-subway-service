package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {
    public static final int ONE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getUpStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        List<Station> stations = getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        boolean isUpStationExisted = isStationExist(upStation, stations);
        boolean isDownStationExisted = isStationExist(downStation, stations);

        verifyAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        updateDownStation(upStation, downStation, distance);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        getDownLineStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        getUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private static void verifyAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExist(Station station, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }

    public void removeSection(Station station) {
        if (isSectionCountLessThanOne()) {
            throw new RuntimeException();
        }

        Section upLineStationSection = getUpStation(station).orElseThrow(IllegalArgumentException::new);
        Section downLineStationSection = getDownLineStation(station).orElseThrow(IllegalArgumentException::new);

        addSections(upLineStationSection, downLineStationSection);

        removeSections(upLineStationSection, downLineStationSection);
    }

    private void removeSections(Section upLineStation, Section downLineStation) {
        sections.remove(upLineStation);
        sections.remove(downLineStation);
    }

    private void addSections(Section upLineStation, Section downLineStation) {
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(this, downLineStation.getUpStation(), upLineStation.getDownStation(), newDistance));
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> getUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private boolean isSectionCountLessThanOne() {
        return sections.size() <= ONE;
    }

}
