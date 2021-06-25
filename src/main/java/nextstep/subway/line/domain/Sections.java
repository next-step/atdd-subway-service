package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
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

    private void updateStation(Section section, Stations stations) {
        if (stations.contains(section.getUpStation())) {
            updateUpStationIfPresent(section);
            return;
        }

        updateDownStationIfPresent(section);
    }

    private void updateUpStationIfPresent(Section section) {
        findSection(it -> it.getUpStation() == section.getUpStation())
            .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStationIfPresent(Section section) {
        findSection(it -> it.getDownStation() == section.getDownStation())
            .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void validateSection(long match) {
        if (match == 2) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (match == 0) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
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
        final List<Station> downStations = sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        return sections.stream()
            .map(Section::getUpStation)
            .filter(it -> !downStations.contains(it))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    private Optional<Station> findNextStation(Station upStation) {
        Optional<Section> maybeSection = findSection(it -> it.getUpStation() == upStation);

        return maybeSection.map(Section::getDownStation);
    }

    public void remove(Station station) {
        validateSectionsSize();

        Optional<Section> maybeUpSection = findSection(it -> it.getDownStation() == station);
        Optional<Section> maybeDownSection = findSection(it -> it.getUpStation() == station);

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

    private Optional<Section> findSection(Predicate<Section> predicate) {
        return sections.stream()
            .filter(predicate)
            .findFirst();
    }
}
