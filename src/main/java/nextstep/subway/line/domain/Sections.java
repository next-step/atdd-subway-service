package nextstep.subway.line.domain;

import nextstep.subway.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
