package nextstep.subway.section.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        List<Station> stations = stations();
        boolean isUpStationExisted = stations.stream()
            .anyMatch(it -> it == section.getUpStation());
        boolean isDownStationExisted = stations.stream()
            .anyMatch(it -> it == section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == section.getUpStation()) &&
            stations.stream().noneMatch(it -> it == section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            findSection(it -> it.getUpStation() == section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(section);
        } else if (isDownStationExisted) {
            sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(section);
        } else {
            throw new RuntimeException();
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
        Optional<Section> maybeSection = findSection(upStation);

        return maybeSection.map(Section::getDownStation);
    }

    private Optional<Section> findSection(Station station) {
        return findSection(it -> it.getUpStation() == station);
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
