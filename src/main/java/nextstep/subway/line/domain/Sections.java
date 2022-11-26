package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(final Section section) {
        this.sections.add(section);
    }

    public Set<Station> getStations() {
        Set<Station> sortedStations = new LinkedHashSet<>();
        Section optionalSection = findFirstSection();
        while (Objects.nonNull(optionalSection)) {
            Section section = optionalSection;
            sortedStations.addAll(section.stations());
            optionalSection = findNextSection(section);
        }
        return sortedStations;
    }

    private Section findFirstSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::downStation)
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(section -> !downStations.contains(section.upStation()))
                .findFirst()
                .orElse(null);
    }

    private Section findNextSection(final Section currentSection) {
        return this.sections.stream()
                .filter(section -> section.isNextOf(currentSection))
                .findFirst()
                .orElse(null);
    }

    public List<Section> value() {
        return sections;
    }

    public void add(final Section section, final Consumer<Section> syncLine) {
        syncLine.accept(section);
        sections.add(section);
    }

    public Section findSameUpStation(final Section section) {
        return sections.stream()
                .filter(section::isSameUpStation)
                .findFirst()
                .orElse(null);
    }

    public Section findSameDownStation(final Section section) {
        return sections.stream()
                .filter(section::isSameDownStation)
                .findFirst()
                .orElse(null);
    }

    public int count() {
        return sections.size();
    }

    public Station findStationById(final long stationId) {
        return getStations().stream()
                .filter(station -> station.getId() == stationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(stationId + "에 해당하는 Station을 찾을 수 없습니다."));
    }

    public void remove(final long stationId, Consumer<Section> syncLine) {
        Station station = findStationById(stationId);
        Section upLineStation = findUpStation(station);
        Section downLineStation = findDownStation(station);

        if (Objects.nonNull(upLineStation) && Objects.nonNull(downLineStation)) {
            Station newUpStation = downLineStation.upStation();
            Station newDownStation = upLineStation.downStation();
            int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
            Section section = new Section(newUpStation, newDownStation, newDistance);
            syncLine.accept(section);
            sections.add(section);
        }

        if (Objects.nonNull(upLineStation)) {
            sections.remove(upLineStation);
        }

        if (Objects.nonNull(downLineStation)) {
            sections.remove(downLineStation);
        }
    }

    private Section findDownStation(final Station station) {
        return sections.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst()
                .orElse(null);
    }

    private Section findUpStation(final Station station) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(station))
                .findFirst()
                .orElse(null);
    }

    public boolean isEmpty() {
        return count() == 0;
    }
}
