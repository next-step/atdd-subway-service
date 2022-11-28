package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.InvalidRequestException;
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

    public Line() {}

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

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station upStation = findUpStation();
        stations.add(upStation);

        while (upStation != null) {
            Station finalUpStation = upStation;
            Optional<Section> nextLineStation = sections.getNextLineStation(finalUpStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            upStation = nextLineStation.get().getDownStation();
            stations.add(upStation);
        }

        return stations;
    }

    public void addStation(Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        checkExistAlreadySection(isUpStationExisted, isDownStationExisted);
        checkValidAddedSection(stations, upStation, downStation);

        if (stations.isEmpty()) {
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            sections.updateUpStation(upStation, downStation, distance);
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isDownStationExisted) {
            sections.updateDownStation(upStation, downStation, distance);
            sections.add(new Section(this, upStation, downStation, distance));
            return;
        }
        throw new InvalidRequestException("추가할수 있는 지하철 역의 구간이 없습니다.");
    }

    private void checkValidAddedSection(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new InvalidRequestException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkExistAlreadySection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new InvalidRequestException("이미 등록된 구간 입니다.");
        }
    }

    private Station findUpStation() {
        Station downStation = sections.getFirstUpStation();

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> beforeLineStation = sections.getBeforeLineStation(finalDownStation);
            if (!beforeLineStation.isPresent()) {
                break;
            }
            downStation = beforeLineStation.get().getUpStation();
        }

        return downStation;
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
        return sections.getList();
    }

}
