package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
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
        addSections(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSections(Section section) {
        sections.addSections(section);
        section.setLine(this);
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

    public void addLineSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations(line);
        boolean isUpStationExisted = checkStationExisted(stations, upStation);
        boolean isDownStationExisted = checkStationExisted(stations, downStation);

        if (stations.isEmpty()) {
            line.getSections().add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted) {
            line.getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            line.getSections().add(new Section(line, upStation, downStation, distance));
        }

        if (isDownStationExisted) {
            line.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            line.getSections().add(new Section(line, upStation, downStation, distance));
        }
    }

    private boolean checkStationExisted(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it.equals(station));
    }

    public List<Station> getStations(Line line) {
        if (line.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;

            if (!sections.isNextLineStation(line, finalDownStation)) {
                break;
            }
            downStation = sections.callNextLineStation(line, finalDownStation).getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            if (!sections.isNextUpstation(line, finalDownStation)) {
                break;
            }
            downStation = sections.callNextUpstation(line, finalDownStation).getUpStation();
        }

        return downStation;
    }

    public void removeLineSection(Line line, Station station) {
        if (line.getSections().size() <= 1) {
            throw new IllegalArgumentException("제거할 구간이 없습니다!");
        }

        if (sections.isNextLineStation(line, station) && sections.isNextUpstation(line, station)) {
            Station newUpStation = sections.callNextUpstation(line, station).getUpStation();
            Station newDownStation = sections.callNextLineStation(line, station).getDownStation();
            int newDistance = sections.callNextLineStation(line, station).getDistance() + sections.callNextUpstation(line, station).getDistance();
            line.getSections().add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        sections.nextLineStation(line, station).ifPresent(it -> line.getSections().remove(it));
        sections.nextUpstation(line, station).ifPresent(it -> line.getSections().remove(it));
    }
}
