package nextstep.subway.line.domain;

import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
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
        List<Station> stations = getOrderdStations();
        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        boolean isUpStationExisted = stations.stream().anyMatch(station -> station.getName().equals(section.getUpStation().getName()));
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station.getName().equals(section.getDownStation().getName()));

        checkAlreadyRegisteredStation(isUpStationExisted, isDownStationExisted);
        checkContainStation(isUpStationExisted, isDownStationExisted);

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
            throw new IllegalArgumentException(ErrorMessage.ALREADY_REGISTERED_LINE);
        }
    }

    private void checkContainStation(boolean isUpStationExisted, boolean isDownStationExisted) {

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new NoSuchElementFoundException(ErrorMessage.NOT_FOUND_STATIONS_FOR_SECTION);
        }
    }

    public List<Station> getOrderdStations() {
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

    public void removeSection(Station deleteStation) {
        checkDeletableSection();

        Optional<Section> upLineStation = findSectionByUpStation(deleteStation);
        Optional<Section> downLineStation = findSectionByDownStation(deleteStation);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            combineSection(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void combineSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();

        sections.add(new Section(upLineStation.getLine(), newUpStation, newDownStation, newDistance));
    }

    private void checkDeletableSection() {
        int DELETABLE_SIZE = 1;
        if (sections.size() <= DELETABLE_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.NOT_DELETABLE_SIZE_SECTION);
        }
    }

    private void updateSectionByUpStation(Station oldStation, Station newStation, int newDistance) {
        Optional<Section> sectionForUpdate = findSectionByUpStation(oldStation);
        sectionForUpdate.ifPresent(section -> section.updateUpStation(newStation, newDistance));
    }

    private void updateSectionByDownStation(Station oldStation, Station newStation, int newDistance) {
        Optional<Section> sectionForUpdate = findSectionByDownStation(oldStation);
        sectionForUpdate.ifPresent(section -> section.updateDownStation(newStation, newDistance));
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream().filter(section -> section.getUpStation().getName().equals(station.getName()))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream().filter(section -> section.getDownStation().getName().equals(station.getName()))
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
