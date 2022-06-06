package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.exception.CannotAddSectionException;
import nextstep.subway.line.domain.exception.CannotDeleteSectionException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {

    }

    List<Station> getStations() {
        if (sectionList.isEmpty()) {
            return Arrays.asList();
        }
        List<Station> stations = new ArrayList<>();
        Section firstSection = this.firstSection();
        stations.add(firstSection.getUpStation());
        stations.addAll(restStations(firstSection));
        return stations;
    }

    private List<Station> restStations(Section firstSection) {
        List<Station> stations = new ArrayList<>();
        Section targetSection = firstSection;
        Optional<Section> nextSection = findNextSection(targetSection);
        while (nextSection.isPresent()) {
            stations.add(nextSection.get().getUpStation());
            targetSection = nextSection.get();
            nextSection = findNextSection(targetSection);
        }
        stations.add(targetSection.getDownStation());
        return stations;
    }

    private Section firstSection(){
        return findFirstSection(sectionList.get(0));
    }

    private Section findFirstSection(Section initSection) {
        Section targetSection = initSection;
        Optional<Section> prevSection = findPrevSection(targetSection);
        while (prevSection.isPresent()) {
            targetSection = prevSection.get();
            prevSection = findPrevSection(targetSection);
        }
        return targetSection;
    }

    private Optional<Section> findPrevSection(Section currentSection){
        return sectionList.stream()
                .filter(section -> section.getDownStation() == currentSection.getUpStation())
                .findFirst();
    }

    private Optional<Section> findNextSection(Section currentSection){
        return sectionList.stream()
                .filter(section -> section.getUpStation() == currentSection.getDownStation())
                .findFirst();
    }

    void addFirstSection(Section newSection) {
        sectionList.add(newSection);
    }

    void addSection(Section newSection) {
        List<Station> stations = this.getStations();
        SectionsStateForAddNewSection state = new SectionsStateForAddNewSection(stations,newSection);
        insertNewSection(newSection, state);
    }

    private void insertNewSection(Section newSection, SectionsStateForAddNewSection state) {
        if (state.isEmptyStations()) {
            addFirstSection(newSection);
            return;
        }
        if (state.isUpStationExisted) {
            insertSectionFromUpStation(newSection);
            return;
        }
        if (state.isDownStationExisted) {
            insertSectionFromDownStation(newSection);
            return;
        }
        throw new CannotAddSectionException("도달 불가능");
    }

    private void insertSectionFromDownStation(Section newSection) {
        sectionList.stream()
                .filter(section -> section.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
        sectionList.add(newSection);
    }

    private void insertSectionFromUpStation(Section newSection) {
        sectionList.stream()
                .filter(section -> section.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
        sectionList.add(newSection);
    }

    void deleteStation(Line line,Station deleteTargetStation) {
        if (sectionList.size() <= 1) {
            throw new CannotDeleteSectionException("노선을 유지하기 위해서는 최소 1구간은 유지되어야 합니다.");
        }
        Optional<Section> upStationMatchedSection = matchUpStation(deleteTargetStation);
        Optional<Section> downStationMatchedSection = matchDownStation(deleteTargetStation);

        addMergedSection(line, upStationMatchedSection, downStationMatchedSection);
        deleteInvalidSection(upStationMatchedSection, downStationMatchedSection);
    }

    private Optional<Section> matchUpStation(Station deleteTargetStation) {
        return sectionList.stream()
                .filter(section -> section.getUpStation() == deleteTargetStation)
                .findFirst();
    }

    private Optional<Section> matchDownStation(Station deleteTargetStation) {
        return sectionList.stream()
                .filter(section -> section.getDownStation() == deleteTargetStation)
                .findFirst();
    }

    private void addMergedSection(Line line, Optional<Section> upStationMatchedSection,
                           Optional<Section> downStationMatchedSection) {
        boolean isMiddleStation = upStationMatchedSection.isPresent() && downStationMatchedSection.isPresent();
        if (isMiddleStation) {
            Station newUpStation = downStationMatchedSection.get().getUpStation();
            Station newDownStation = upStationMatchedSection.get().getDownStation();
            int newDistance =
                    upStationMatchedSection.get().getDistance() + downStationMatchedSection.get().getDistance();
            sectionList.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void deleteInvalidSection(Optional<Section> upStationMatchedSection, Optional<Section> downStationMatchedSection) {
        upStationMatchedSection.ifPresent(section -> sectionList.remove(section));
        downStationMatchedSection.ifPresent(section -> sectionList.remove(section));
    }

    private class SectionsStateForAddNewSection{
        private List<Station> stations;
        private boolean isUpStationExisted;
        private boolean isDownStationExisted;

        private SectionsStateForAddNewSection(List<Station> stations, Section newSection) {
            this.stations = stations;
            this.isUpStationExisted = stations.stream()
                    .anyMatch(station -> station == newSection.getUpStation());
            this.isDownStationExisted = stations.stream()
                    .anyMatch(station -> station == newSection.getDownStation());
            verify();
        }

        private void verify() {
            if (isUpStationExisted && isDownStationExisted) {
                throw new CannotAddSectionException("이미 등록된 구간 입니다.");
            }
            if (!stations.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
                throw new CannotAddSectionException("등록할 수 없는 구간 입니다.");
            }
        }

        private boolean isEmptyStations(){
            return stations.isEmpty();
        }
    }
}
