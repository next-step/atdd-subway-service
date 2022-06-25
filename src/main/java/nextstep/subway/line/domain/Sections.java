package nextstep.subway.line.domain;

import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private static final int DELETABLE_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        List<Station> stations = getOrderStations();
        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        boolean isUpStationExisted = stations.stream().anyMatch(station -> station.equals(section.getUpStation()));
        boolean isDownStationExisted = stations.stream().anyMatch(station -> station.equals(section.getDownStation()));

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

    public List<Station> getOrderStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
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

    public boolean hasStation(Station station) {
        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        return upLineStation.isPresent() || downLineStation.isPresent();
    }

    private void combineSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();

        sections.add(new Section(upLineStation.getLine(), newUpStation, newDownStation, newDistance));
    }

    private void checkDeletableSection() {
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
        return sections.stream().filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream().filter(section -> section.getDownStation().equals(station))
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
