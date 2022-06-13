package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.common.Messages.ALREADY_REGISTERED_STATION;
import static nextstep.subway.common.Messages.UNREGISTERED_STATION;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validate(section);
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        if (matchUpStation(upStation)) {
            updateUpStation(section);
        }

        if (matchDownStation(downStation)) {
            updateDownStation(section);
        }

        this.sections.add(section);
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

    public boolean isNextUpSection(Station station) {
        return sections.stream()
                .anyMatch(section -> section.isEqualsUpStation(station));
    }

    public boolean isNextDownSection(Station station) {
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

    private void validate(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Stations stations = findStations();

        List<Station> distinctStations = stations.distinctStations();
        boolean isUpStationExisted = distinctStations.contains(section.getUpStation());
        boolean isDownStationExisted = distinctStations.contains(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ALREADY_REGISTERED_STATION);
        }

        if (stations.isNotEmpty() && distinctStations.contains(upStation) && distinctStations.contains(downStation)) {
            throw new RuntimeException(UNREGISTERED_STATION);
        }
    }

    private boolean matchUpStation(Station station) {
        return sections.stream().anyMatch(section -> section.isEqualsUpStation(station));
    }

    private boolean matchDownStation(Station station) {
        return sections.stream().anyMatch(section -> section.isEqualsDownStation(station));
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
}
