package nextstep.subway.line.domain;

import nextstep.subway.line.message.SectionMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MINIMUM_NUMBER_OF_SECTION = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sectionItems;

    public Sections() {
        this.sectionItems = new ArrayList<>();
    }

    public static Sections of(List<Section> sectionItems) {
        Sections sections = new Sections();
        sections.sectionItems.addAll(new ArrayList<>(sectionItems));
        return sections;
    }

    public void add(Section section) {
        if(this.sectionItems.isEmpty()) {
            this.sectionItems.add(section);
            return;
        }
        validateNewSection(section);
        updateSameUpStationSection(section);
        updateSameDownStationSection(section);
        this.sectionItems.add(section);
    }

    public void removeStation(Station station) {
        validateRemoveStation();

        Optional<Section> upLineStation = getSameUpStationSection(station);
        Optional<Section> downLineStation = getSameDownStationSection(station);
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section newSection = Section.merge(downLineStation.get(), upLineStation.get());
            this.sectionItems.add(newSection);
        }

        upLineStation.ifPresent(this.sectionItems::remove);
        downLineStation.ifPresent(this.sectionItems::remove);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Optional<Section> sectionOptional = getUpTerminalSection();
        while(sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            addUpAndDownStations(stations, section);
            sectionOptional = getSameUpStationSection(section.getDownStation());
        }
        return Collections.unmodifiableList(new ArrayList<>(stations));
    }

    private void validateNewSection(Section section) {
        List<Station> stations = getStations();
        if(isAlreadyEnrolledStations(stations, section)) {
            throw new IllegalArgumentException(SectionMessage.ADD_ERROR_ALREADY_ENROLLED_STATIONS.message());
        }

        if(isNotFoundStations(stations, section)) {
            throw new IllegalArgumentException(SectionMessage.ADD_ERROR_NONE_MATCH_SECTION_STATIONS.message());
        }
    }

    private boolean isAlreadyEnrolledStations(List<Station> stations, Section section) {
        return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
    }

    private boolean isNotFoundStations(List<Station> stations, Section section) {
        return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
    }

    private void updateSameDownStationSection(Section section) {
        this.sectionItems.stream()
                .filter(sectionItem -> sectionItem.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(sectionItem -> sectionItem.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateSameUpStationSection(Section section) {
        this.sectionItems.stream()
                .filter(sectionItem -> sectionItem.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(sectionItem -> sectionItem.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void validateRemoveStation() {
        if (this.sectionItems.size() <= MINIMUM_NUMBER_OF_SECTION) {
            throw new IllegalArgumentException(SectionMessage.REMOVE_ERROR_MORE_THAN_TWO_SECTIONS.message());
        }
    }

    private Optional<Section> getSameUpStationSection(Station station) {
        return this.sectionItems.stream()
                .filter(section -> section.isSameUpStation(station))
                .findFirst();
    }

    private Optional<Section> getSameDownStationSection(Station station) {
        return this.sectionItems.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst();
    }

    private Optional<Section> getUpTerminalSection() {
        Set<Station> downStations = this.sectionItems.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
        return this.sectionItems.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst();
    }

    private void addUpAndDownStations(List<Station> stations, Section section) {
        if(!stations.contains(section.getUpStation())) {
            stations.add(section.getUpStation());
        }

        if(!stations.contains(section.getDownStation())) {
            stations.add(section.getDownStation());
        }
    }

    public List<Section> getAll() {
        return this.sectionItems;
    }
}
