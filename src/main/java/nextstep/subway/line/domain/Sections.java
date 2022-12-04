package nextstep.subway.line.domain;

import nextstep.subway.line.message.SectionMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sectionItems;

    public Sections() {
        this.sectionItems = new ArrayList<>();
    }

    public void add(Section section) {
        if(this.sectionItems.isEmpty()) {
            this.sectionItems.add(section);
            return;
        }
        List<Station> stations = getStations();
        validateNewSection(stations, section);
        updateSameUpStationSection(section);
        updateSameDownStationSection(section);
        this.sectionItems.add(section);
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

    private void validateNewSection(List<Station> stations, Section section) {
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

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        if(this.sectionItems.isEmpty()) {
            return stations;
        }

        Optional<Section> sectionOptional = Optional.of(getUpTerminalSection());
        while(sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            addStations(stations, section);
            sectionOptional = getNextSection(section);
        }
        return Collections.unmodifiableList(new ArrayList<>(stations));
    }

    private void addStations(List<Station> stations, Section section) {
        if(!stations.contains(section.getUpStation())) {
            stations.add(section.getUpStation());
        }

        if(!stations.contains(section.getDownStation())) {
            stations.add(section.getDownStation());
        }
    }

    private Section getUpTerminalSection() {
        Set<Station> downStations = this.sectionItems.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
        return this.sectionItems.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Optional<Section> getNextSection(Section section) {
        return this.sectionItems.stream()
                .filter(sectionItem -> sectionItem.isSameUpStation(section.getDownStation()))
                .findFirst();
    }

    public void removeStation(Line line, Station station) {
        if (this.sectionItems.size() <= 1) {
            throw new IllegalArgumentException(SectionMessage.REMOVE_ERROR_MORE_THAN_TWO_SECTIONS.message());
        }

        Optional<Section> upLineStation = this.sectionItems.stream()
                .filter(section -> section.isSameUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = this.sectionItems.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance upLineDistance = upLineStation.get().getDistance();
            Distance downLineDistance = downLineStation.get().getDistance();
            Distance newDistance = upLineDistance.plus(downLineDistance);
            this.sectionItems.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(this.sectionItems::remove);
        downLineStation.ifPresent(this.sectionItems::remove);
    }
}
