package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.ErrMsg;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int NON_DELETABLE_SECTION_COUNT = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateAddSection(section);
        if (!addFirstOrLastSection(section)) {
            addSectionInMiddle(section);
        }
    }

    private void addSectionInMiddle(Section section) {
        if (!addUpMatchingSection(section)) {
            addDownMatchingSection(section);
        }
    }

    private boolean addUpMatchingSection(Section section) {
        Optional<Section> target = getUpMatchingSection(section.getUpStation());
        if (target.isPresent()) {
            target.get().updateUpStation(section);
            sections.add(section);
            return true;
        }
        return false;
    }
    private void addDownMatchingSection(Section section) {
        Section target = getDownMatchingSection(section.getDownStation()).get();
        target.updateDownStation(section);
        sections.add(section);
    }


    private Optional<Section> getUpMatchingSection(Station station) {
        return sections.stream().filter(it -> it.getUpStation().equals(station)).findFirst();
    }

    private Optional<Section> getDownMatchingSection(Station station) {
        return sections.stream().filter(it -> it.getDownStation().equals(station)).findFirst();
    }

    private void validateAddSection(Section section) {
        if (!isExists(section.getDownStation()) && !isExists(section.getUpStation())) {
            throw new IllegalStateException(ErrMsg.STATIONS_NOT_EXISTS);
        }
        if (isExists(section.getDownStation()) && isExists(section.getUpStation())) {
            throw new IllegalStateException(ErrMsg.STATIONS_ALREADY_EXISTS);

        }
    }

    private boolean addFirstOrLastSection(Section section) {
        if (isFirstStation(section.getDownStation()) || isLastStation(section.getUpStation())) {
            sections.add(section);
            return true;
        }
        return false;
    }

    public boolean isExists(Station station) {
        return getAllStations().stream().anyMatch(it -> it.equals(station));
    }
    private List<Station> getAllStations() {
        return sections.stream().map(Section::getStations).flatMap(Collection::stream).distinct()
                .collect(Collectors.toList());
    }

    private boolean isLastStation(Station station) {
        if (sections.isEmpty()) {
            return false;
        }
        return getLastDownStation().equals(station);
    }

    private boolean isFirstStation(Station station) {
        if (sections.isEmpty()) {
            return false;
        }
        return getFirstUpStation().equals(station);
    }


    public List<Section> getAll() {
        return sections;
    }

    private List<Station> getUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public Station getFirstUpStation() {
        List<Station> stations = getUpStations();
        stations.removeAll(getDownStations());
        return stations.get(0);
    }

    public Station getLastDownStation() {
        List<Station> stations = getDownStations();
        stations.removeAll(getUpStations());
        return stations.get(0);
    }

    public List<Station> getOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (sections.isEmpty()) {
            return orderedStations;
        }
        Station currentStation = getFirstUpStation();
        orderedStations.add(currentStation);
        while (orderedStations.size()-1 < sections.size()) {
            currentStation = getNextStation(currentStation);
            orderedStations.add(currentStation);
        }
        return Collections.unmodifiableList(orderedStations);
    }

    private Station getNextStation(Station station) {
        return getUpMatchingSection(station).get().getDownStation();
    }


    public void deleteStation(Station station) {
        validateDeleteCondition();
        if (!deleteFirstOrLastStation(station)) {
            deleteMiddleStation(station);
        }
    }

    private void validateDeleteCondition() {
        if (sections.size() <= NON_DELETABLE_SECTION_COUNT) {
            throw new IllegalStateException(ErrMsg.CANNOT_DELETE_SECTION_WHEN_ONE);
        }
    }

    private boolean deleteFirstOrLastStation(Station station) {
        if (getFirstUpStation().equals(station)) {
            sections.remove(0);
            return true;
        }
        if (getLastDownStation().equals(station)) {
            sections.remove(sections.size() - 1);
            return true;
        }
        return false;
    }
    private void deleteMiddleStation(Station station) {
        getUpMatchingSection(station).ifPresent(it -> deleteMiddleSection(it));
    }
    private void deleteMiddleSection(Section section) {
        Section target = getDownMatchingSection(section.getUpStation()).get();
        target.updateDownStationDelete(section.getDownStation(), section.getDistance());
        this.sections.remove(section);
    }

    public boolean contains(Section... sectionsInput) {
        return this.sections.containsAll(Arrays.asList(sectionsInput));
    }

}
