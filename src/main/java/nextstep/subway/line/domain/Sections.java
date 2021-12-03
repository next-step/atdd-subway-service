package nextstep.subway.line.domain;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final Integer MIN_LINE_STATION_SIZE = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public static Sections of() {
        return new Sections();
    }

    public void add(Section section) {
        validateDuplicate(section);
        validateAddAblePosition(section);
        updateUpStationIfSameUpStation(section);
        updateDownStationIfSameDownStation(section);

        sections.add(section);
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        return mapStations(downStation, stations);
    }

    public void remove(Station station) {
        validateDeleteSize();

        Optional<Section> downSection = sections.stream()
            .filter(it -> it.isSameUpStation(station))
            .findFirst();
        Optional<Section> upSection = sections.stream()
            .filter(it -> it.isSameDownStation(station))
            .findFirst();

        if (upSection.isPresent() && downSection.isPresent()) {
            addOfMerge(upSection.get(), downSection.get());
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    public Integer count() {
        return sections.size();
    }

    private List<Station> mapStations(Station downStation, List<Station> stations) {
        stations.add(downStation);
        Optional<Section> nextLineStation = findNextStation(downStation);

        return nextLineStation.map(section -> mapStations(section.getDownStation(), stations))
            .orElse(stations);
    }

    private Station findUpStation() {
        Station firstUpStation = getFirstUpStation();
        return findFirstUpStation(firstUpStation);
    }

    private Station findFirstUpStation(Station firstUpStation) {
        Station finalDownStation = firstUpStation;
        Optional<Section> nextLineStation = sections.stream()
            .filter(it -> it.getDownStation() == firstUpStation)
            .findFirst();

        if (nextLineStation.isPresent()) {
            finalDownStation = findFirstUpStation(nextLineStation.get().getUpStation());
        }

        return finalDownStation;
    }

    private Optional<Section> findNextStation(Station finalDownStation) {
        return sections.stream()
            .filter(it -> it.isSameUpStation(finalDownStation))
            .findFirst();
    }

    private void updateUpStationIfSameUpStation(Section section) {
        sections.stream()
            .filter(it -> it.isSameUpStation(section.getUpStation()))
            .findFirst()
            .ifPresent(it -> it.updateUpStationOf(section));
    }

    private void updateDownStationIfSameDownStation(Section section) {
        sections.stream()
            .filter(it -> it.isSameDownStation(section.getDownStation()))
            .findFirst()
            .ifPresent(it -> it.updateDownStationOf(section));
    }

    private void validateDuplicate(Section section) {
        if (isDuplicatedSection(section)) {
            throw new InvalidParameterException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddAblePosition(Section section) {
        if (isAddAblePosition(section)) {
            throw new InvalidParameterException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isAddAblePosition(Section section) {
        List<Station> stations = getStationsInOrder();
        return !sections.isEmpty() && stations.stream().noneMatch(section::isSameUpStation)
            && stations.stream().noneMatch(section::isSameDownStation);
    }

    private boolean isDuplicatedSection(Section section) {
        return sections.stream()
            .anyMatch(section::isSameUpStationAndDownStation);
    }


    private void addOfMerge(Section upSection, Section downSection) {
        sections.add(upSection.newOfMerge(downSection));
    }

    private void validateDeleteSize() {
        if (sections.size() == MIN_LINE_STATION_SIZE) {
            throw new InvalidParameterException("구간이 하나 일 경우 제거 할 수 없습니다.");
        }
    }

    private Station getFirstUpStation() {
        return sections.get(0).getUpStation();
    }
}
