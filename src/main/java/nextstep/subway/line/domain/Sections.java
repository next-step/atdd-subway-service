package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(section.getDownStation()));
        throwIfSectionAlreadyExist(isUpStationExisted, isDownStationExisted);
        throwIfNoStationExistInSections(section, stations);
        addSection(section, stations, isUpStationExisted, isDownStationExisted);
    }

    private void addSection(Section newSection, List<Station> stations, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (CollectionUtils.isEmpty(stations)) {
            addSectionIfSectionIsEmpty(newSection);
            return;
        }
        if (isUpStationExisted) {
            addSectionUpStationIsExisted(newSection);
            return;
        }
        if (isDownStationExisted) {
            addSectionDownStationIsExisted(newSection);
            return;
        }
        throw new IllegalArgumentException(MESSAGE_EXCEPTION_CAN_NOT_ADD_SECTION);
    }

    private void addSectionDownStationIsExisted(Section newSection) {
        findSectionHasSameDownStation(newSection.getDownStation())
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
        sections.add(newSection);
    }

    private Optional<Section> findSectionHasSameDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(station))
                .findFirst();
    }

    private void addSectionUpStationIsExisted(Section newSection) {
        findSectionHasSameUpStation(newSection.getUpStation())
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
        sections.add(newSection);
    }

    private Optional<Section> findSectionHasSameUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(station))
                .findFirst();
    }

    private void addSectionIfSectionIsEmpty(Section section) {
        sections.add(section);
    }

    private Station findUpEndStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = findSectionHasSameDownStation(downStation);
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
            Optional<Section> nextLineStation = findSectionHasSameUpStation(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private void throwIfNoStationExistInSections(Section newSection, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it.equals(newSection.getUpStation())) &&
                stations.stream().noneMatch(it -> it.equals(newSection.getDownStation()))) {
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
        Optional<Section> upLineStation = findSectionHasSameUpStation(station);
        Optional<Section> downLineStation = findSectionHasSameDownStation(station);
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
