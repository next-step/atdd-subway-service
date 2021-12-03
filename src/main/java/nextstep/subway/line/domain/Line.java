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

    private int additionalFee;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int additionalFee) {
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int additionalFee) {
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
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
        return sections.getSections();
    }

    public List<Station> getStations() {
        if (getSections().isEmpty()) {
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = sections.getSectionUpStationSame(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findUpStation() {

        Station downStation = sections.getEndUpStation();
        while (downStation != null) {
            Station nextStation = nextStation(downStation);
            if (nextStation == null) {
                break;
            }
            downStation = nextStation;
        }
        return downStation;
    }

    private Station nextStation(Station station) {
        return sections.getSectionDownStationSame(station)
                .map(Section::getUpStation)
                .orElse(null);
    }

    public void addSection(Section section) {

        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        checkSectionExists(isUpStationExisted, isDownStationExisted);
        if (!stations.isEmpty()){
            checkStationAddable(isUpStationExisted, isDownStationExisted);
        }
        if (stations.isEmpty()) {
            getSections().add(new Section(this, upStation, downStation, distance));
            return;
        }
        if (isUpStationExisted) {
            addSectionUpStationExist(upStation, downStation, distance);
        }
        if (isDownStationExisted) {
            addSectionDownStationExists(upStation, downStation, distance);
        }
    }

    private void checkSectionExists(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void checkStationAddable(boolean isUpStationExisted, boolean isDownStationExisted) {
        if(!isUpStationExisted && !isDownStationExisted){
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void addSectionUpStationExist(Station upStation, Station downStation, int distance) {
        sections.updateUpStation(upStation, downStation, distance);
        getSections().add(new Section(this, upStation, downStation, distance));
    }

    private void addSectionDownStationExists(Station upStation, Station downStation, int distance) {

        sections.updateDownStation(upStation,downStation, distance);
        getSections().add(new Section(this, upStation, downStation, distance));
    }

    public void removeLineStation(Station station) {

        checkSectionRemovable();
        Optional<Section> upLineStation = sections.getSectionUpStationSame(station);
        Optional<Section> downLineStation = sections.getSectionDownStationSame(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addNewSection(upLineStation.get(), downLineStation.get());
        }
        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    private void checkSectionRemovable() {
        if (getSections().size() <= 1) {
            throw new IllegalArgumentException("제거 가능한 구간이 없습니다.");
        }
    }

    private void addNewSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
    }

    public int getAdditionalFee() {
        return additionalFee;
    }


    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                ", additionalFee=" + additionalFee +
                '}';
    }
}
