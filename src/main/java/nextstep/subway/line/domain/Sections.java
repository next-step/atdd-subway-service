package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    protected Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public List<Section> getList() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return getStationsOrderByUpToDown();
    }

    private List<Station> getStationsOrderByUpToDown() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findPrimaryUpStation();
        stations.add(downStation);

        while (hasUpStation(downStation)) {
            Section nextLineStation = getNextSectionByUpStation(downStation);
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Section getNextSectionByUpStation(final Station downStation) {
        return sections.stream()
                .filter(it -> it.hasUpStation(downStation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Station findPrimaryUpStation() {
        return findAllUpStation().stream()
                .filter(station -> !hasDownStation(station))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private boolean hasUpStation(final Station station) {
        return findAllUpStation().contains(station);
    }

    private boolean hasDownStation(final Station station) {
        return findAllDownStation().contains(station);
    }

    private Set<Station> findAllUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toSet());
    }

    private Set<Station> findAllDownStation() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
    }
}
