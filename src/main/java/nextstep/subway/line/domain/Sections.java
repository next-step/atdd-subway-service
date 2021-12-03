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
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    protected Sections () {
        this.sections = new ArrayList<>();
    }

    public void add(Section section) {
        if (!contains(section)) {
            sections.add(section);
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Stations getStations () {
        return Stations.from(
            this.sections.stream()
            .sorted()
            .map(section -> section.getUpDownStations())
            .flatMap(stations -> stations.stream())
            .distinct()
            .collect(Collectors.toList()));
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public void updateSection(Station upStation, Station downStation, int distance) {
        updateByUpStation(upStation, downStation, distance);
        updateByDownStation(upStation, downStation, distance);
    }

    private void updateByUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
            .filter(it -> it.isUpStation(upStation))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateByDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
            .filter(it -> it.isDownStation(downStation))
            .findFirst()
            .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    @Override
    public String toString() {
        return "Sections{" +
            "sections=" + sections +
            '}';
    }
}
