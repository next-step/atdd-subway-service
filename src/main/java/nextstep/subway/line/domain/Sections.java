package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() { }

    protected void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validateAdd(section);

        resizeNearSections(section);

        sections.add(section);
    }

    private void validateAdd(Section section) {
        if (containsByUpStation(section) && containsByDownStation(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!containsByUpStation(section) && !containsByDownStation(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void resizeNearSections(Section section) {
        if (containsByUpStation(section)) {
            updateUpStationBySameUpStation(section);
        } else if (containsByDownStation(section)) {
            updateDownStationBySameDownStation(section);
        }
    }
    protected Optional<Section> removeStation(Station station) {
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

    private Optional<Section> createNewSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());

            return Optional.of(new Section(newUpStation, newDownStation, newDistance));
        }
        return Optional.empty();
    }

    private Optional<Section> findByUpStationEquals(Station station) {
        return findFirst(item -> item.isUpStationEquals(station));
    }

    private Optional<Section> findByDownStationEquals(Station station) {
        return findFirst(item -> item.isDownStationEquals(station));
    }

    private boolean containsByUpStation(Section section) {
        return anyMatch(item -> item.containsByUpStation(section));
    }

    private boolean containsByDownStation(Section section) {
        return anyMatch(item -> item.containsByDownStation(section));
    }

    private void updateUpStationBySameUpStation(Section section) {
        findFirst(item -> item.isSameUpStation(section))
                .ifPresent(item -> item.updateUpStation(section));
    }

    private void updateDownStationBySameDownStation(Section section) {
        findFirst(item -> item.isSameDownStation(section))
                .ifPresent(item -> item.updateDownStation(section));
    }

    private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> remove(it));
        downLineStation.ifPresent(it -> remove(it));
    }

    private void remove(Section section) {
        sections.remove(section);
    }

    private Optional<Section> findFirst(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findFirst();
    }

    private boolean anyMatch(Predicate<Section> predicate) {
        return sections.stream()
                .anyMatch(predicate);
    }
}
