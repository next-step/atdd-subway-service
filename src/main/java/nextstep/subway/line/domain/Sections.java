package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Stream<Section> stream() {
        return sections.stream();
    }

    public void add(Section section) {
        if (section != null) {
            sections.add(section);
        }
    }

    public void remove(Section section) {
        if (section != null) {
            sections.remove(section);
        }
    }

    public void addWithValidationAndReassign(Section sectionToAdd) {
        reAssignSection(sectionToAdd);
        sections.add(sectionToAdd);
    }

    private void reAssignSection(Section sectionToAdd) {
        sections.stream()
                .filter(section -> section.matchSamePositionStation(sectionToAdd))
                .findFirst()
                .ifPresent(section -> section.splitSection(sectionToAdd));
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }
        Stations stations = new Stations();
        Station downStation = findFinalUpStation();
        while (downStation != null) {
            stations.add(downStation);
            downStation = nextStationOf(downStation, StationPosition.DOWN_STATION);
        }
        return stations;
    }

    public Station nextStationOf(Station station, StationPosition stationPosition) {
        Station nextStation = null;
        Section nextSection = sections.stream()
                .filter(section -> section.isStationOppositeOf(station, stationPosition))
                .findFirst()
                .orElse(null);
        if (nextSection != null) {
            nextStation = nextSection.getStationByPosition(stationPosition);
        }
        return nextStation;
    }

    public Station findFinalUpStation() {
        Station finalUpStation = null;
        Station nextUpstation = null;
        if (!sections.isEmpty()) {
            nextUpstation = sections.get(0).getDownStation();
        }
        while (nextUpstation != null) {
            finalUpStation = nextUpstation;
            nextUpstation = nextStationOf(finalUpStation, StationPosition.UP_STATION);
        }
        return finalUpStation;
    }

    public Section getMatchSectionByPosition(Station station, StationPosition stationPosition) {
        return sections.stream()
                .filter(section -> section.isStationMatchWithPositionOf(station, stationPosition))
                .findFirst().orElse(null);
    }

    public List<Section> getSections() {
        return sections;
    }
}
