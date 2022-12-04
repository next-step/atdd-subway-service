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
        Station newSectionUpStation = section.getUpStation();
        Station newSectionDownStation = section.getDownStation();
        int newSectionDistance = section.getDistance();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it.equals(newSectionUpStation));
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it.equals(newSectionDownStation));

        if(isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(SectionMessage.ADD_ERROR_ALREADY_ENROLLED_STATIONS.message());
        }

        if(!isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException(SectionMessage.ADD_ERROR_NONE_MATCH_SECTION_STATIONS.message());
        }

        if(isUpStationExisted) {
            this.sectionItems.stream()
                    .filter(sectionItem -> sectionItem.isSameUpStation(newSectionUpStation))
                    .findFirst()
                    .ifPresent(sectionItem -> sectionItem.updateUpStation(newSectionDownStation, newSectionDistance));
            this.sectionItems.add(section);
        }

        if(isDownStationExisted) {
            this.sectionItems.stream()
                    .filter(sectionItem -> sectionItem.isSameDownStation(newSectionDownStation))
                    .findFirst()
                    .ifPresent(sectionItem -> sectionItem.updateDownStation(newSectionUpStation, newSectionDistance));
            this.sectionItems.add(section);
        }
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
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sectionItems.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(this.sectionItems::remove);
        downLineStation.ifPresent(this.sectionItems::remove);
    }
}
