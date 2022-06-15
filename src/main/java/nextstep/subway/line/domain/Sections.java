package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.common.Messages.*;

@Embeddable
public class Sections {
    private static int MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        validateAddSection(section);
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        if (hasNextUpSection(upStation)) {
            updateUpStation(section);
        }

        if (hasNextDownSection(downStation)) {
            updateDownStation(section);
        }

        this.sections.add(section);
    }

    public void removeSectionByStation(Station station) {
        validateRemoveStation(station);

        if (hasUpStationAndDownStation(station)) {
            removeUpStationAndDownStation(station);
            return;
        }

        if (hasNextUpSection(station)) {
            matchAndRemoveUpStation(station);
            return;
        }

        matchAndRemoveDownStation(station);
    }

    public List<Section> getSections() {
        return sections;
    }

    public Stations findStations() {
        Stations stations = new Stations();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }

    public boolean hasNextUpSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualsUpStation(station));
    }

    public boolean hasNextDownSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualsDownStation(station));
    }

    public Section findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(station))
                .findFirst()
                .orElse(null);
    }

    public Section findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsDownStation(station))
                .findFirst()
                .orElse(null);
    }

    private void validateAddSection(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Stations stations = findStations();

        List<Station> distinctStations = stations.distinctStations();
        boolean isUpStationExisted = distinctStations.contains(section.getUpStation());
        boolean isDownStationExisted = distinctStations.contains(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(ALREADY_REGISTERED_STATION);
        }

        if (stations.isNotEmpty() && !distinctStations.contains(upStation) && !distinctStations.contains(downStation)) {
            throw new IllegalArgumentException(UNREGISTERED_STATION);
        }
    }

    private void validateRemoveStation(Station station) {
        if (sections.size() <= MINIMUM_SIZE) {
            throw new IllegalArgumentException(LEAST_ONE_MUST_EXIST_REMOVE_STATION);
        }

        if (!hasNextUpSection(station) && !hasNextDownSection(station)) {
            throw new IllegalArgumentException(NOT_MATCH_REMOVE_STATION);
        }
    }

    private void updateUpStation(Section section) {
        Station upStation = section.getUpStation();
        Section findSection = findSectionByUpStation(upStation);
        findSection.updateUpStation(section.getDownStation(), section.getDistance());
    }

    private void updateDownStation(Section section) {
        Station downStation = section.getDownStation();
        Section findSection = findSectionByDownStation(downStation);
        findSection.updateDownStation(section.getUpStation(), section.getDistance());
    }

    private boolean hasUpStationAndDownStation(Station station) {
        return hasNextUpSection(station) && hasNextDownSection(station);
    }

    private void removeUpStationAndDownStation(Station station) {
        Section sectionByUpStation = findSectionByUpStation(station);
        Section sectionByDownStation = findSectionByDownStation(station);

        Section section = Section.of(
                sectionByUpStation.getLine(),
                sectionByDownStation.getUpStation(),
                sectionByUpStation.getDownStation(),
                sectionByUpStation.getDistance() + sectionByDownStation.getDistance()
        );

        sections.add(section);
        sections.remove(sectionByUpStation);
        sections.remove(sectionByDownStation);
    }

    private Section matchAndRemoveUpStation(Station station) {
        Section upStation = findSectionByUpStation(station);
        sections.remove(upStation);
        return upStation;
    }

    private Section matchAndRemoveDownStation(Station station) {
        Section downStation = findSectionByDownStation(station);
        sections.remove(downStation);
        return downStation;
    }
}
