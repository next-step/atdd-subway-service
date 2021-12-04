package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections () {
    }

    public void add(Section section) {
        if (!contains(section)) {
            sections.add(section);
        }
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Station> getStations () {
        return this.sections.stream()
            .sorted()
            .map(section -> section.getUpDownStations())
            .flatMap(stations -> stations.stream())
            .distinct()
            .collect(Collectors.toList());
    }

    public void remove(Section section) {
        section.removeLine();
        sections.remove(section);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public boolean anyMatchStation(Station station) {
        return getStations().stream().anyMatch(it -> it.equals(station));
    }

    public boolean noneMatchStation(Station station) {
        return getStations().stream().noneMatch(it -> it.equals(station));
    }

    public boolean isEmptyStation() {
        return getStations().isEmpty();
    }

    public boolean isMinSize() {
        return sections.size() <= MIN_SIZE;
    }

    public Optional<Section> findByUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.isUpStation(station))
            .findFirst();
    }

    public Optional<Section> findByDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.isDownStation(station))
            .findFirst();
    }

    public void updateSection(Station upStation, Station downStation, int distance) {
        updateByUpStation(upStation, downStation, distance);
        updateByDownStation(upStation, downStation, distance);
    }

    private void updateByUpStation(Station upStation, Station downStation, int distance) {
        findByUpStation(upStation)
            .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateByDownStation(Station upStation, Station downStation, int distance) {
        findByDownStation(downStation)
            .ifPresent(it -> it.updateDownStation(upStation, distance));
    }
}
