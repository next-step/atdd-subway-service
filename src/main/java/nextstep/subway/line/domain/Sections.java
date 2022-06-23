package nextstep.subway.line.domain;

import static nextstep.subway.exception.domain.SubwayExceptionMessage.DUPLICATE_SECTION;
import static nextstep.subway.exception.domain.SubwayExceptionMessage.NOT_REGISTER_SECTION;
import static nextstep.subway.exception.domain.SubwayExceptionMessage.NOT_REMOVE_SECTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.generic.domain.Distance;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int AVAILABLE_REMOVE_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        rearrangeSectionAndAdd(section);
    }

    private void rearrangeSectionAndAdd(final Section section) {
        validateSection(section);
        sections.stream()
                .filter(origin -> origin.intersects(section))
                .findFirst()
                .ifPresent(origin -> origin.rearrange(section));

        sections.add(section);
    }

    private void validateSection(final Section section) {
        if (hasSection(section)) {
            throw new SubwayException(DUPLICATE_SECTION);
        }

        if (isNotContinuousSection(section)) {
            throw new SubwayException(NOT_REGISTER_SECTION);
        }
    }

    private boolean hasSection(final Section section) {
        return sections.stream().anyMatch(origin -> origin.equalsStations(section));
    }

    private boolean isNotContinuousSection(final Section section) {
        return findAllStations().stream()
                .noneMatch(section::hasStation);
    }

    public List<Section> getList() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return getStationsOrderByUpToDown();
    }

    private List<Station> getStationsOrderByUpToDown() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findPrimaryUpStation();
        stations.add(downStation);

        while (hasUpStation(downStation)) {
            Section nextLineStation = getNextSectionByUpStation(downStation);
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Section getNextSectionByUpStation(final Station downStation) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(downStation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private Station findPrimaryUpStation() {
        return findAllUpStation().stream()
                .filter(station -> !hasDownStation(station))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private boolean hasUpStation(final Station station) {
        return findAllUpStation().contains(station);
    }

    private boolean hasDownStation(final Station station) {
        return findAllDownStation().contains(station);
    }

    private Set<Station> findAllStations() {
        return Stream.concat(findAllUpStation().stream(), findAllDownStation().stream())
                .collect(Collectors.toSet());
    }

    private Set<Station> findAllUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toSet());
    }

    private Set<Station> findAllDownStation() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());
    }

    public void removeStation(final Station station) {
        validateRemoveStation();

        mergeSections(station);
        removeUpStationSection(station);
        removeDownStationSection(station);
    }

    private void validateRemoveStation() {
        if (isAvailableRemove()) {
            throw new SubwayException(NOT_REMOVE_SECTION);
        }
    }

    private boolean isAvailableRemove() {
        return sections.size() <= AVAILABLE_REMOVE_SIZE;
    }

    private void mergeSections(final Station station) {
        if (hasBetweenStation(station)) {
            sections.add(Section.mergeSection(findSectionByDownStation(station), findSectionByUpStation(station)));
        }
    }

    private boolean hasBetweenStation(final Station station) {
        return hasUpStation(station) && hasDownStation(station);
    }

    private void removeUpStationSection(final Station station) {
        if (hasUpStation(station)) {
            sections.remove(findSectionByUpStation(station));
        }
    }

    private Section findSectionByUpStation(final Station upStation) {
        return sections.stream()
                .filter(section -> section.equalsUpStation(upStation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void removeDownStationSection(final Station station) {
        if (hasDownStation(station)) {
            sections.remove(findSectionByDownStation(station));
        }
    }

    private Section findSectionByDownStation(final Station downStation) {
        return sections.stream()
                .filter(section -> section.equalsDownStation(downStation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Distance totalDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Distance::plus)
                .orElseThrow(() -> new IllegalArgumentException("거리값이 잘못 되었습니다."));
    }

    public boolean containsStations(Section section) {
        return sections.stream()
                .anyMatch(source -> source.containsStations(section));
    }

    public Section bindDistance(Section section) {
        return sections.stream()
                .filter(item -> containsStations(section))
                .findFirst()
                .map(section::bindDistance)
                .orElseThrow(() -> new NoSuchElementException("구간을 찾을 수 없습니다."));
    }

    public void foreach(Consumer<Section> consumer) {
        sections.forEach(consumer);
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sections)) {
            return false;
        }
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
