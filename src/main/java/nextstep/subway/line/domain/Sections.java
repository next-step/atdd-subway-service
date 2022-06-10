package nextstep.subway.line.domain;

import nextstep.subway.exception.SectionNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> list;

    public Sections() {
        this.list = new ArrayList<>();
    }

    public Sections(List<Section> list) {
        this.list = list;
    }

    public void add(Section section) {
        this.list.add(section);
    }

    public List<Section> getList() {
        return this.list;
    }

    public Station getLineUpStation() {
        Set<Station> stationSet = getStationSet();
        this.list.forEach(section -> stationSet.remove(section.getDownStation()));
        return findFirstStation(stationSet);
    }

    public Station getLineDownStation() {
        Set<Station> stationSet = getStationSet();
        this.list.forEach(section -> stationSet.remove(section.getUpStation()));
        return findFirstStation(stationSet);
    }

    public Optional<Section> findSectionWithUpStation(Station upStation) {
        return this.list.stream()
                .filter(section -> upStation.equals(section.getUpStation()))
                .findFirst();
    }

    public Optional<Section> findSectionWithDownStation(Station downStation) {
        return this.list.stream()
                .filter(section -> downStation.equals(section.getDownStation()))
                .findFirst();
    }

    public List<Station> getStations() {
        if (this.list.isEmpty()) {
            return Arrays.asList();
        }

        Station curStation = getLineUpStation();
        Station lineDownStation = getLineDownStation();

        List<Station> stations = new ArrayList<>();
        stations.add(curStation);
        while (!curStation.equals(lineDownStation)) {
            Section section = findSectionWithUpStation(curStation)
                    .orElseThrow(SectionNotFoundException::new);
            curStation = section.getDownStation();
            stations.add(curStation);
        }

        return stations;
    }

    private Set<Station> getStationSet() {
        Set<Station> stationSet = new HashSet<>();
        for (Section section : this.list) {
            stationSet.add(section.getUpStation());
            stationSet.add(section.getDownStation());
        }
        return stationSet;
    }

    private Station findFirstStation(Set<Station> stationSet) {
        return stationSet.stream()
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }
}
