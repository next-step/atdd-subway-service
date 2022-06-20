package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> list;

    protected Sections() {
        this.list = new ArrayList<>();
    }

    public static Sections instance() {
        return new Sections();
    }

    public void add(final Section section) {
        list.add(section);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(list);
    }

    public List<Station> getAllStations() {
        final Set<Station> stations = new HashSet<>();
        for (Section section : list) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return Collections.unmodifiableList(stations.stream()
                .collect(Collectors.toList()));
    }

}
