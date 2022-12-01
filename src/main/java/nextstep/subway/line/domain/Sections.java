package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final String MESSAGE_EXCEPTION_SECTION_IS_ALREADY_EXIST = "이미 등록된 구간 입니다.";
    private static final String MESSAGE_EXCEPTION_SECTION_CAN_NOT_BE_ADDED = "등록할 수 없는 구간 입니다.";
    private static final String MESSAGE_EXCEPTION_CAN_NOT_ADD_SECTION = "구간을 추가할 수 없습니다";
    private static final String MESSAGE_EXCEPTION_STATION_TO_DELETE_NOT_EXIST_IN_SECTIONS = "삭제하려는 지하철역이 구간에 존재하지 않습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return this.sections;
    }

    public List<StationResponse> getStationResponse() {
        List<Station> stations = getStations();
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<Station> allStations = new ArrayList<>();
        allStations.add(findUpEndStation());
        allStations.addAll(findAllStationsInDownDirectionAfterStation(allStations.get(0)));
        return allStations;
    }

    public void add(Section section) {
        sections.add(section);
    }

    private Station findUpEndStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private List<Station> findAllStationsInDownDirectionAfterStation(Station startStation) {
        List<Station> stations = new ArrayList<>();
        Station downStation = startStation;
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
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

    public SectionAdder getSectionAdder(Station upStation, Station downStation) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(upStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(downStation));
        throwIfSectionAlreadyExist(isUpStationExisted, isDownStationExisted);
        throwIfNoStationExistInSections(upStation, downStation, stations);
        return createSectionAdder(stations, isUpStationExisted, isDownStationExisted);
    }

    private SectionAdder createSectionAdder(List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (stations.isEmpty()) {
            return new EmptySectionAdder();
        }
        if (isUpStationExisted) {
            return new FromUpperSectionAdder();
        }
        if (isDownStationExisted) {
            return new FromDownSectionAdder();
        }
        throw new IllegalArgumentException(MESSAGE_EXCEPTION_CAN_NOT_ADD_SECTION);
    }

    private void throwIfNoStationExistInSections(Station upStation, Station downStation, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(upStation)) &&
                stations.stream().noneMatch(it -> it.equals(downStation))) {
            throw new RuntimeException(MESSAGE_EXCEPTION_SECTION_CAN_NOT_BE_ADDED);
        }
    }

    private void throwIfSectionAlreadyExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(MESSAGE_EXCEPTION_SECTION_IS_ALREADY_EXIST);
        }
    }

    public void remove(Station station) {
        throwIfSectionSizeIsOne();
        throwIfStationNotExistInSections(station);
        Optional<Section> upLineStation = getSections().stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
        Optional<Section> downLineStation = getSections().stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
        mergeSectionIfStationIsCenterOfTwoLineStation(upLineStation, downLineStation);
        removeSectionIfPresent(upLineStation, downLineStation);
    }

    private void throwIfStationNotExistInSections(Station station) {
        getStations().stream().filter(stationInLine -> stationInLine.equals(station))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(MESSAGE_EXCEPTION_STATION_TO_DELETE_NOT_EXIST_IN_SECTIONS));
    }

    private void throwIfSectionSizeIsOne() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private void removeSectionIfPresent(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> getSections().remove(it));
        downLineStation.ifPresent(it -> getSections().remove(it));
    }

    private void mergeSectionIfStationIsCenterOfTwoLineStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            getSections().add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }
    }

    public int size() {
        return this.sections.size();
    }
}
