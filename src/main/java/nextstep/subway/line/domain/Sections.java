package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public List<Section> getList() {
        return sections;
    }

    public boolean addSection(final Section section) {
        return sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        final List<Station> sortedStations = new ArrayList<>();
        final Station rootStation = searchRootStation();
        sortedStations.add(rootStation);

        Optional<Station> nextStation = findNextStation(rootStation);
        while (nextStation.isPresent()) {
            final Station next = nextStation.get();
            sortedStations.add(next);
            nextStation = findNextStation(next);
        }
        return sortedStations;
    }

    private Station searchRootStation() {
        final Set<Station> upStations = findUpStations();
        final Set<Station> downStations = findDownStations();
        return upStations.stream()
                .filter(upStation ->
                        !downStations.contains(upStation))
                .findFirst().get();
    }

    private Set<Station> findUpStations() {
        return sections.stream()
                .map(
                        section -> section.getUpStation())
                .collect(
                        Collectors.toSet());
    }

    private Set<Station> findDownStations() {
        return sections.stream()
                .map(section ->
                        section.getDownStation())
                .collect(Collectors.toSet());
    }

    private Optional<Station> findNextStation(final Station station) {
        final Optional<Section> nextSection = sections.stream()
                .filter(section ->
                        section.isEqualToUpStation(station))
                .findFirst();
        return nextSection.map(Section::getDownStation);
    }
}
