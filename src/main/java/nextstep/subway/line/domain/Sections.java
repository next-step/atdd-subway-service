package nextstep.subway.line.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        List<Station> stations = this.getStations();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Distance distance = section.getDistance();

        checkValid(stations, upStation, downStation);
        addInnerSection(section.getLine(), stations, upStation, downStation, distance);
    }

    public List<Station> getStations() {
        if (this.isSectionEmpty()) {
            return Arrays.asList();
        }
        return getOrderedStations();
    }

    public void removeStation(Line line, Station station) {
        checkValidRemoveStation();
        changeRemoveSection(line, station);
    }

    private void changeRemoveSection(Line line, Station station) {

        boolean isExistUpStation = checkExistUpSection(station);
        boolean isExistDownStation = checkExistDownSection(station);

        if (isMiddleStation(isExistUpStation, isExistDownStation)) {
            Section upLineStation = findUpSection(station);
            Section downLineStation = findDownSection(station);
            Station newUpStation = downLineStation.getUpStation();
            Station newDownStation = upLineStation.getDownStation();
            Distance newDistance = upLineStation.getDistance().sum(downLineStation.getDistance());

            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));

            this.sections.remove(upLineStation);
            this.sections.remove(downLineStation);
        }
    }

    private void checkValidRemoveStation() {
        if (hasLastOneSection()) {
            throw new RuntimeException();
        }
    }

    private boolean hasLastOneSection() {
        return this.sections.size() <= 1;
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
        return isMiddleStation(isUpStationExisted, isDownStationExisted);
    }

    private boolean isMiddleStation(boolean isExistUpStation, boolean isExistDownStation) {
        return isExistUpStation && isExistDownStation;
    }

    private void addInnerSection(Line line, List<Station> stations, Station upStation, Station downStation, Distance distance) {
        if (stations.isEmpty()) {
            this.sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        addUpdatedUpStation(line, stations, upStation, downStation, distance);
        addUpdatedDownStation(line, stations, upStation, downStation, distance);
    }

    private void addUpdatedDownStation(Line line, List<Station> stations, Station upStation, Station downStation, Distance distance) {
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);
        if (!isDownStationExisted) {
            return;
        }
        if (checkExistDownSection(downStation)) {
            findDownSection(downStation).updateDownStation(upStation, distance);
        }
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    private void addUpdatedUpStation(Line line, List<Station> stations, Station upStation, Station downStation, Distance distance) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        if (!isUpStationExisted) {
            return;
        }
        if (checkExistUpSection(upStation)) {
            findUpSection(upStation).updateUpStation(downStation, distance);
        }
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    private boolean checkExistUpSection(Station station) {
        return this.sections.stream()
                .anyMatch(it -> it.getUpStation() == station);
    }

    private boolean checkExistDownSection(Station station) {
        return this.sections.stream()
                .anyMatch(it -> it.getDownStation() == station);
    }

    private Section findUpSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_SECTION));
    }

    private Section findDownSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_SECTION));
    }

    public List<Section> sections() {
        return this.sections;
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public boolean isSectionEmpty() {
        return this.sections.isEmpty();
    }
}
