package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    public static final int ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    protected Sections() {}

    public List<Section> get() {
        return sections;
    }

    public List<Station> getStations() {
        Map<Station, Station> stations = sectionsToMap();
        return sortStations(stations, findUpStation(stations));
    }

    private Map<Station, Station> sectionsToMap() {
        return sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Station findUpStation(Map<Station, Station> stations) {
        return stations.keySet()
                .stream()
                .filter(upStation -> !stations.containsValue(upStation))
                .findFirst()
                .orElseThrow(() -> new NoResultException("상행 종착역이 존재하지 않습니다."));
    }

    private List<Station> sortStations(Map<Station, Station> stations, Station upStation) {
        List<Station> sortedStations = new ArrayList<>();

        Station currentStation = upStation;
        while (currentStation != null) {
            sortedStations.add(currentStation);
            currentStation = stations.get(currentStation);
        }

        return sortedStations;
    }

    public void add(Section newSection) {
        sections.add(newSection);
    }
}
