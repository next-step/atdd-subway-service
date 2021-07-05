package nextstep.subway.line.domain;

import nextstep.subway.line.exception.UnaddableSectionException;
import nextstep.subway.line.exception.UndeletableStationInSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    protected Sections() {

    }

    public Sections(List<Section> sections) {
        this.values = sections;
    }

    public void add(Section section) {
        validateConnectableSection(section);
        addSectionByCase(section);
    }

    public List<Section> get() {
        return this.values;
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Stations toStations() {
        if (values.isEmpty()) {
            return new Stations(emptyList());
        }
        Stations result = new Stations();
        if (!isEmpty()) {
            Section section = values.get(0);
            result.addAll(getUpStations(section.getUpStation()));
            result.addAll(getDownStations(section.getDownStation()));
        }

        return result;
    }

    public void deleteStation(Station deletingStation) {
        validateDeletableStation();
        validateExistingStation(deletingStation);
        deleteStationByCase(deletingStation);
    }

    public Distance sumDistances() {
        Distance result = new Distance(1);

        for (Section value : values) {
            result = result.add(value.getDistance());
        }

        return result.minus(new Distance(1));
    }

    private void validateConnectableSection(Section section) {
        int result = getConnectedStationCount(section);

        validateExistingSection(result);
        validateUnConnectableSection(result);
    }

    private int getConnectedStationCount(Section section) {
        Stations stations = toStations();

        return (int) stations.get().stream()
                .filter(section::isIncludeStation)
                .count();
    }

    private void validateExistingSection(long result) {
        final long ILLEGAL_OVERLAPPED_STATION_COUNT = 2L;

        if (result == ILLEGAL_OVERLAPPED_STATION_COUNT) {
            throw new UnaddableSectionException("기존에 등록된 구간과 중복됩니다.");
        }
    }

    private void validateUnConnectableSection(long result) {
        final long ILLEGAL_UN_CONNECTABLE_STATION_COUNT = 0L;

        if (!isEmpty() && result == ILLEGAL_UN_CONNECTABLE_STATION_COUNT) {
            throw new UnaddableSectionException("요청하신 구간은 연결이 불가합니다.");
        }
    }

    private void addSectionByCase(Section section) {
        if (isEmpty() || isStartOrEndStationInLine(section)) {
            this.values.add(section);
            return;
        }
        connectSectionAtExistingSection(section);
    }

    private boolean isStartOrEndStationInLine(Section section) {
        Stations stations = toStations();
        Station startStation = stations.get(0);
        Station endStation = stations.get(stations.lastIndex());

        return section.isSameStationWithDownStation(startStation)
                || section.isSameStationWithUpStation(endStation);
    }

    private void connectSectionAtExistingSection(Section section) {
        Optional<Section> foundSection = values.stream()
                .filter(value -> value.isSameStationWithUpStation(section.getUpStation())
                        || value.isSameStationWithDownStation(section.getDownStation()))
                .findFirst();

        foundSection.ifPresent(connectedSection -> {
            connectedSection.connectSectionBetween(section);
            values.add(section);
        });
    }

    private Stations getUpStations(Station upStation) {
        LinkedList<Station> result = new LinkedList<>();
        Station foundUpStation = upStation;
        while (foundUpStation != null) {
            result.addFirst(foundUpStation);
            foundUpStation = findUpStation(foundUpStation).orElse(null);
        }
        return new Stations(result);
    }

    private Stations getDownStations(Station downStation) {
        List<Station> result = new ArrayList<>();
        Station foundDownStation = downStation;
        while (foundDownStation != null) {
            result.add(foundDownStation);
            foundDownStation = findDownStation(foundDownStation).orElse(null);
        }
        return new Stations(result);
    }

    private Optional<Station> findUpStation(Station upStation) {
        return values.stream()
                .filter(section -> section.getDownStation().getId().equals(upStation.getId()))
                .map(Section::getUpStation)
                .findFirst();
    }

    private Optional<Station> findDownStation(Station downStation) {
        return values.stream()
                .filter(section -> section.getUpStation().getId().equals(downStation.getId()))
                .map(Section::getDownStation)
                .findFirst();
    }

    private void validateDeletableStation() {
        if (this.size() == 1) {
            throw new UndeletableStationInSectionException("노선의 구간이 하나일 때는 지울 수 없습니다.");
        }
    }

    private void validateExistingStation(Station removingStation) {
        if(!toStations().contains(removingStation)) {
            throw new UndeletableStationInSectionException("이 역이 노선에 존재 하지 않습니다.");
        }
    }

    private void deleteStationByCase(Station deletingStation) {
        final int SORTING_UP_SECTION = -1;
        final int SORTING_DOWN_SECTION = 1;

        List<Section> result = values.stream()
                .filter(value -> (value.getUpStation().equals(deletingStation)
                        || value.getDownStation().equals(deletingStation)))
                .sorted((left, right) -> left.getDownStation().equals(deletingStation) ? SORTING_UP_SECTION : SORTING_DOWN_SECTION)
                .collect(toList());

        deleteStartOrEndStation(result);
        deleteMiddleStation(result);
    }

    private void deleteStartOrEndStation(List<Section> filteredSections) {
        final int STANDARD_OF_START_OR_END_STATION = 1;

        if (filteredSections.size() == STANDARD_OF_START_OR_END_STATION) {
            values.remove(filteredSections.get(0));
        }
    }

    private void deleteMiddleStation(List<Section> filteredSections) {
        final int STANDARD_OF_MIDDLE_STATION = 2;
        if (filteredSections.size() == STANDARD_OF_MIDDLE_STATION) {
            Section upSection = filteredSections.get(0);
            Section downSection = filteredSections.get(1);
            upSection.mergeSection(downSection);
            values.remove(downSection);
        }
    }

    @Override
    public String toString() {
        return "Sections{" +
                "values=" + values +
                '}';
    }
}
