package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    private final static List<Station> CACHE = new ArrayList<>();

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public void add(Section section) {
        elements.add(section);
    }

    public List<Section> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public List<Station> getStations() {
        if (elements.isEmpty()) {
            return CACHE;
        }

        Stations stations = new Stations();
        stations.connectStation(getElements());

        return stations.getElements();
    }
}
