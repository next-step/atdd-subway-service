package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.domain.exception.CannotAddSectionException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    List<Section> getSectionList() {
        return sections;
    }

    List<Station> getStations() {
        if (sections.isEmpty()) {
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
        return findFirstSection(sections.get(0));
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
        return sections.stream()
                .filter(section -> section.getDownStation() == currentSection.getUpStation())
                .findFirst();
    }

    private Optional<Section> findNextSection(Section currentSection){
        return sections.stream()
                .filter(section -> section.getUpStation() == currentSection.getDownStation())
                .findFirst();
    }

    void addFirstSection(Section newSection) {
        sections.add(newSection);
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
        throw new RuntimeException("도달 불가능");
    }

    private void insertSectionFromDownStation(Section newSection) {
        sections.stream()
                .filter(section -> section.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
        sections.add(newSection);
    }

    private void insertSectionFromUpStation(Section newSection) {
        sections.stream()
                .filter(section -> section.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
        sections.add(newSection);
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
