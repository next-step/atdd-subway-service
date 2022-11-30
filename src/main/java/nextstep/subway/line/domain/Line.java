package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.station.domain.Station;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.exception.type.ValidExceptionType.ALREADY_EXIST_LINE_STATION;
import static nextstep.subway.exception.type.ValidExceptionType.STATION_BOTH_NOT_EXIST;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }

        if (StringUtils.hasText(color)) {
            this.color = color;
        }
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    private void validCheckSectionSize() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    public void removeLineStation(Station station) {
        validCheckSectionSize();

        Optional<Section> upLineStation = this.getSections().stream().filter(it -> it.getUpStation() == station).findFirst();
        Optional<Section> downLineStation = this.getSections().stream().filter(it -> it.getDownStation() == station).findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSection(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);
    }

    private void remove(Section section) {
        this.sections.remove(section);
    }

    private void addSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        Distance newDistance = upLineStation.plusDistance(downLineStation);
        Section section = new Section(this, newUpStation, newDownStation, newDistance);
        this.addSection(section);
    }


    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(this);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream().filter(it -> it.getUpStation() == finalDownStation).findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream().filter(it -> it.getDownStation() == finalDownStation).findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
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
        return sections;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validCheckAlreadyBothExistStation(isUpStationExisted, isDownStationExisted);
        validCheckAddRejectSection(stations, upStation, downStation);

        if (stations.isEmpty()) {
            this.addSection(new Section(this, upStation, downStation, Distance.from(distance)));
            return;
        }

        addSectionIfStationExisted(isUpStationExisted, isDownStationExisted, upStation, downStation, distance);
    }

    private void addSectionIfStationExisted(boolean isUpStationExisted, boolean isDownStationExisted, Station upStation, Station downStation, int distance) {
        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
            this.addSection(new Section(this, upStation, downStation, Distance.from(distance)));
        }

        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
            this.addSection(new Section(this, upStation, downStation, Distance.from(distance)));
        }
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        this.getSections()
                .stream()
                .filter(it -> it.isSameDownStation(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        this.getSections()
                .stream()
                .filter(it -> it.isSameUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void validCheckAddRejectSection(List<Station> stations, Station upStation, Station downStation) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) && stations.stream().noneMatch(it -> it == downStation)) {
            throw new NotValidDataException(STATION_BOTH_NOT_EXIST.getMessage());
        }
    }

    private void validCheckAlreadyBothExistStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new NotValidDataException(ALREADY_EXIST_LINE_STATION.getMessage());
        }
    }
}
