package nextstep.subway.line.domain;

import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

        checkAlreadyRegisteredStation(isUpStationExisted, isDownStationExisted);
        checkValidSection(section);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            updateSectionByUpStation(section.getUpStation(), section.getDownStation(), section.getDistance());
            sections.add(section);
        }

        if (isDownStationExisted) {
            updateSectionByDownStation(section.getDownStation(), section.getUpStation(), section.getDistance());
            sections.add(section);
        }
    }

    private void checkAlreadyRegisteredStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ErrorMessage.ALREADY_REGISTERED_LINE.getMessage());
        }
    }

    private void checkValidSection(Section section) {
        List<Station> stations = getStations();

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
                stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException(ErrorMessage.NOT_FOUND_STATIONS_FOR_SECTION.getMessage());
        }
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        if (getSections().isEmpty()) {
            return stations;
        }

        Station station = getFirstStation();
        stations.add(station);

        Optional<Section> nextSection = findSectionByUpStation(station);
        while (nextSection.isPresent()) {
            station = nextSection.get().getDownStation();
            stations.add(station);

            nextSection = findSectionByUpStation(station);
        }

        return stations;
    }

    private void updateSectionByUpStation(Station oldStation, Station newStation, int newDistance) {
        Optional<Section> sectionForUpdate = findSectionByUpStation(oldStation);
        sectionForUpdate.ifPresent(it -> it.updateUpStation(newStation, newDistance));
    }

    private void updateSectionByDownStation(Station oldStation, Station newStation, int newDistance) {
        Optional<Section> sectionForUpdate = findSectionByDownStation(oldStation);
        sectionForUpdate.ifPresent(it -> it.updateDownStation(newStation, newDistance));
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream().filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream().filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Station getFirstStation() {
        Station station = sections.get(0).getUpStation();

        Optional<Section> nextSection = findSectionByDownStation(station);
        while (nextSection.isPresent()) {
            station = nextSection.get().getUpStation();

            nextSection = findSectionByDownStation(station);
        }

        return station;
    }
}
