package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() { }

    public Sections(List<Section> sections) {
        for (Section section : sections) {
            add(section);
        }
    }

    protected void add(Section section) {
        if (sections.contains(section)) {
            return;
        }

        sections.add(section);
    }

    protected NewSection removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findByUpStationEquals(station);
        Optional<Section> downLineStation = findByDownStationEquals(station);

        removeSection(upLineStation, downLineStation);

        return createNewSection(upLineStation, downLineStation);
    }

    protected SortedStations toSortedStations() {
        return new SortedStations(sections);
    }

    protected List<Section> toCollection() {
        return sections;
    }

    private NewSection createNewSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();

            return new NewSection(newUpStation, newDownStation, newDistance);
        }
        return null;
    }

    private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> remove(it));
        downLineStation.ifPresent(it -> remove(it));
    }

    private Optional<Section> findByUpStationEquals(Station station) {
        return sections.stream()
                .filter(item -> item.isUpStationEquals(station))
                .findFirst();
    }

    private Optional<Section> findByDownStationEquals(Station station) {
        return sections.stream()
                .filter(item -> item.isDownStationEquals(station))
                .findFirst();
    }

    private void remove(Section section) {
        sections.remove(section);
    }
}
