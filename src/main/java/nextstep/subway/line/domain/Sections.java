package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        requireNonNull(section, "section");
        sections.add(section);
    }

    public List<Section> get() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return getStationInOrder();
    }

    private List<Station> getStationInOrder() {
        Map<Station, Station> map = sections.stream()
                                            .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        while (map.get(station) != null) {
            stations.add(station);
            station = map.get(station);
        }
        stations.add(station);
        return stations;
    }

    private Station findUpStation() {
        Set<Station> upStations = findUpStationOfSection();
        Set<Station> downStations = findDownStationOfSection();
        return upStations.stream()
                         .filter(upStation -> !downStations.contains(upStation))
                         .findFirst()
                         .orElseThrow(IllegalArgumentException::new);
    }

    private Set<Station> findUpStationOfSection() {
        return sections.stream()
                       .map(Section::getUpStation)
                       .collect(Collectors.toSet());
    }

    private Set<Station> findDownStationOfSection() {
        return sections.stream()
                       .map(Section::getDownStation)
                       .collect(Collectors.toSet());
    }
}
