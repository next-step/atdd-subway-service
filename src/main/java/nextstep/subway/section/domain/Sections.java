package nextstep.subway.section.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> toList() {
        return sections;
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new LinkedList<>();
        final Station upStation = firstUpStation();
        stations.add(upStation);
        Optional<Station> maybeNextStation = findNextStation(upStation);

        while (maybeNextStation.isPresent()) {
            final Station station = maybeNextStation.get();
            stations.add(station);
            maybeNextStation = findNextStation(station);
        }

        return stations;
    }

    private Station firstUpStation() {
        return sections.get(0).getUpStation();
    }

    private Optional<Station> findNextStation(Station upStation) {
        Optional<Section> maybeSection = findSection(upStation);

        return maybeSection.map(Section::getDownStation);
    }

    private Optional<Section> findSection(Station station) {
        return sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
    }
}
