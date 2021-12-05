package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean isSectionEmpty() {
        return this.sections.isEmpty();
    }

    public List<Station> getStations() {
        if (this.isSectionEmpty()) {
            return Arrays.asList();
        }
        return getOrderedStations();
    }

    public void addSection(Section section) {
        List<Station> stations = this.getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        int distance = section.getDistance();

        checkValid(stations, upStation, downStation);
        addInnerSection(stations, upStation, downStation, distance);
    }

    public void removeStation(Station station) {
        checkValidRemoveStation();

        Optional<Section> upLineStation = this.getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        changeRemoveSection(upLineStation, downLineStation);

        upLineStation.ifPresent(it -> this.getSections().remove(it));
        downLineStation.ifPresent(it -> this.getSections().remove(it));
    }

    private void changeRemoveSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (hasBothUpDownStation(upLineStation, downLineStation)) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
        }
    }

    private boolean hasBothUpDownStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        return upLineStation.isPresent() && downLineStation.isPresent();
    }

    private void checkValidRemoveStation() {
        if (hasLastOneSection()) {
            throw new RuntimeException();
        }
    }

    private boolean hasLastOneSection() {
        return this.getSections().size() <= 1;
    }

    private void addInnerSection(List<Station> stations, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (stations.isEmpty()) {
            this.getSections().add(new Section(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            addUpdatedUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            addUpdatedDownStation(upStation, downStation, distance);
        }
    }

    private void addUpdatedDownStation(Station upStation, Station downStation, int distance) {
        this.getSections().stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        this.getSections().add(new Section(this, upStation, downStation, distance));
    }

    private void addUpdatedUpStation(Station upStation, Station downStation, int distance) {
        this.getSections().stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        this.getSections().add(new Section(this, upStation, downStation, distance));
    }


    private List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();

        Section foundSection = findFirstSection();
        Station lastStation = findLastStation();
        orderedStations.add(foundSection.getUpStation());
        while (!isLastSection(foundSection, lastStation)) {
            Section finalFoundSection = foundSection;
            orderedStations.add(finalFoundSection.getDownStation());
            foundSection = this.sections.stream()
                    .filter(it -> it.getUpStation() == finalFoundSection.getDownStation())
                    .findFirst()
                    .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_SECTION));
        }
        orderedStations.add(lastStation);
        return orderedStations;
    }

    private Station findLastStation() {
        List<Station> upStations = findUpStations();
        List<Station> downStations = findDownStations();
        return downStations.stream()
                .filter(it -> !upStations.contains(it))
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));
    }

    private boolean isLastSection(Section foundSection, Station lastStation) {
        return foundSection.getDownStation().equals(lastStation);
    }


    private boolean hasFoundSection(Section foundSection) {
        return Optional.ofNullable(foundSection).isPresent() && Optional.ofNullable(foundSection.getDownStation()).isPresent();
    }

    private Section findFirstSection() {
        Station firstStation = findFirstStation();
        return this.sections.stream()
                .filter(it -> it.getUpStation() == firstStation)
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_SECTION));
    }

    private Station findFirstStation() {
        List<Station> upStations = findUpStations();
        List<Station> downStations = findDownStations();
        return upStations.stream()
                .filter(it -> !downStations.contains(it))
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));
    }

    private List<Station> findUpStations() {
        return this.sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private List<Station> findDownStations() {
        return this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private void checkValid(List<Station> stations, Station upStation, Station downStation) {
        if (hasSameUpDownStation(stations, upStation, downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (hasNoneStations(stations, upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean hasNoneStations(List<Station> stations, Station upStation, Station downStation) {
        return !stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation);
    }

    private boolean hasSameUpDownStation(List<Station> stations, Station upStation, Station downStation) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);
        return isUpStationExisted && isDownStationExisted;
    }
}
