package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.exception.CannotRemoveException;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.line.exception.TerminusNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.NoSuchStationException;

@Embeddable
public class Sections {

    private static final int REDUCIBLE_COUNT = 2;
    private static final int SIZE_LOWER_LIMIT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public List<Station> getStationsInOrder() {
        List<Station> stations = new ArrayList<>();

        Station station = findUpStation();
        stations.add(station);

        Optional<Section> maybeNext = getNextSection(station);
        while (maybeNext.isPresent()) {
            Section section = maybeNext.get();
            stations.add(section.getDownStation());

            maybeNext = getNextSection(section.getDownStation());
        }

        return stations;
    }

    private Optional<Section> getNextSection(Station station) {
        return values.stream()
            .filter(it -> it.getUpStation() == station)
            .findAny();
    }

    private Station findUpStation() {
        Set<Station> downStations = values.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
        return values
            .stream()
            .map(Section::getUpStation)
            .filter(station -> !downStations.contains(station))
            .findAny()
            .orElseThrow(() -> new TerminusNotFoundException("출발역이 존재하지 않습니다."));
    }

    public void addSection(Section section) {
        checkSection(section);

        updateSection(section);
        values.add(section);
    }

    private void updateSection(Section section) {
        values.stream()
            .filter(s -> s.matchesOnlyOneEndWith(section))
            .findAny()
            .ifPresent(oldSection -> {
                values.remove(oldSection);
                values.add(oldSection.shiftedBy(section));
            });
    }

    private void checkSection(Section section) {
        if (Objects.isNull(section)) {
            throw new IllegalArgumentException("null 인 구간을 추가할 수는 없습니다.");
        }

        if (!values.isEmpty() && allOrNothingMatches(section)) {
            throw new InvalidSectionException("한쪽 역만 노선에 존재해야 합니다.");
        }
    }

    private boolean allOrNothingMatches(Section section) { // XOR Existence Check
        List<Station> stations = getStationsInOrder();
        return stations.contains(section.getUpStation())
                == stations.contains(section.getDownStation());
    }

    public void removeStation(Station station) {
        checkStation(station);

        Sections sections = new Sections();
        values.stream()
            .filter(s -> s.contains(station))
            .forEach(sections::addSection);

        values.add(sections.reduce());
        values.removeAll(sections.values);
    }

    private Section reduce() {
        checkReducible();
        return values.stream()
            .reduce(Section::mergeWith)
            .orElseThrow(() -> new CannotRemoveException("구간 축약에 실패했습니다."));
    }

    private void checkReducible() {
        if (!isReducible()) {
            throw new CannotRemoveException("3개 이상의 구간을 축약할 수 없습니다.");
        }
    }

    private boolean isReducible() {
        return values.size() <= REDUCIBLE_COUNT;
    }

    private void checkStation(Station station) {
        if (!contains(station)) {
            throw new NoSuchStationException("노선에 해당 지하철 역이 존재하지 않습니다.");
        }

        if (!isRemovable()) {
            throw new CannotRemoveException("마지막 남은 구간은 삭제할 수 없습니다.");
        }
    }

    private boolean contains(Station station) {
        return getStationsInOrder().contains(station);
    }

    private boolean isRemovable() {
        return values.size() > SIZE_LOWER_LIMIT;
    }
}
