package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationPair;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(final List<Section> sections) {
        this.sections.addAll(sections);
    }

    public void add(final Section section) {
        final List<Station> stationList = stations();

        if (stationList.isEmpty()) {
            sections.add(section);
            return;
        }

        final Stations stations = new Stations(stationList);
        final long match = stations.countMatch(Arrays.asList(section.getUpStation(), section.getDownStation()));
        validateSection(match);
        updateStation(section, stations);
        sections.add(section);
    }

    private void updateStation(final Section section, final Stations stations) {
        if (stations.contains(section.getUpStation())) {
            updateUpStationIfPresent(section);
            return;
        }

        updateDownStationIfPresent(section);
    }

    private void updateUpStationIfPresent(final Section section) {
        findSection(it -> it.equalsUpStation(section.getUpStation()))
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStationIfPresent(final Section section) {
        findSection(it -> it.equalsDownStation(section.getDownStation()))
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void validateSection(final long match) {
        if (match == 2) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (match == 0) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> toList() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Station> stations = new LinkedList<>();
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

    public List<Station> mergeStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        final Set<Station> stations = new HashSet<>();
        sections.forEach(it -> stations.addAll(it.toList()));

        return new ArrayList<>(stations);
    }

    private Station firstUpStation() {
        final List<Station> downStations = sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        return sections.stream()
            .map(Section::getUpStation)
            .filter(it -> !downStations.contains(it))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private Optional<Station> findNextStation(final Station upStation) {
        final Optional<Section> maybeSection = findSection(it -> it.equalsUpStation(upStation));

        return maybeSection.map(Section::getDownStation);
    }

    public void remove(final Station station) {
        validateSectionsSize();

        final Optional<Section> maybeUpSection = findSection(it -> it.equalsDownStation(station));
        final Optional<Section> maybeDownSection = findSection(it -> it.equalsUpStation(station));

        if (maybeUpSection.isPresent() && maybeDownSection.isPresent()) {
            sections.add(Section.of(maybeUpSection.get(), maybeDownSection.get()));
        }

        maybeDownSection.ifPresent(sections::remove);
        maybeUpSection.ifPresent(sections::remove);
    }

    private void validateSectionsSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }

    private Optional<Section> findSection(final Predicate<Section> predicate) {
        return sections.stream()
            .filter(predicate)
            .findFirst();
    }

    public int getMaxExtraFare(final List<StationPair> stationPairs) {
        return stationPairs.stream()
            .mapToInt(this::findExtraFare)
            .max()
            .orElseThrow(IllegalArgumentException::new);
    }

    private Integer findExtraFare(final StationPair it) {
        return sections.stream()
            .filter(section -> section.equalsUpStation(it.getUpStation()) &&
                section.equalsDownStation(it.getDownStation()))
            .map(Section::getExtraFare)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
