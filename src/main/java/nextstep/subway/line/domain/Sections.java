package nextstep.subway.line.domain;

import com.google.common.collect.Lists;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.utils.Message.DUPLICATED_SECTION;
import static nextstep.subway.utils.Message.INVALID_SECTION;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections initSections() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public List<Section> getSections() { return Collections.unmodifiableList(sections); }

    public void add(Section section) {
        checkUniqueSection(section);
        checkValidSection(section);
        updateUpStation(section);
        updateDownStation(section);
        sections.add(section);
    }

    private void checkUniqueSection(Section newSection) {
        if (this.sections.contains(newSection) || sections.stream().anyMatch(it -> newSection.isSameSection(it))) {
            throw new RuntimeException(DUPLICATED_SECTION);
        }
    }

    private void checkValidSection(Section section) {
        List<Station> stations = this.getStations();

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> section.isUpStation(it)) &&
                stations.stream().noneMatch(it -> section.isDownStation(it))) {
            throw new RuntimeException(INVALID_SECTION);
        }
    }

    private void updateUpStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(newSection::isUpStation);

        if (isUpStationExisted) {
            this.sections.stream()
                    .filter(se -> newSection.isUpStation(se.getUpStation()))
                    .findFirst()
                    .ifPresent(se -> se.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

        }
    }

    private void updateDownStation(Section newSection) {
        List<Station> stations = this.getStations();
        boolean isDownStationExisted = stations.stream().anyMatch(newSection::isDownStation);

        if (isDownStationExisted) {
            this.sections.stream()
                    .filter(se -> newSection.isDownStation(se.getDownStation()))
                    .findFirst()
                    .ifPresent(se-> se.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            // Collections.EMPTY_LIST 타입 안정성 고려 -> Collections.emptyList
            return Collections.emptyList();
        }

        Station currentStation = findFirstStationOfLine();
        List<Station> stations = Lists.newArrayList(currentStation);

        Optional<Station> nextStation = findAfterStation(currentStation);

        while (nextStation.isPresent()) {
            currentStation = nextStation.get();
            stations.add(currentStation);
            nextStation = findAfterStation(currentStation);
        }

        return stations;
    }

    private Station findFirstStationOfLine() {
        Station currentUpStation = sections.get(0).getUpStation();
        Optional<Station> prevStation = findBeforeStation(currentUpStation);

        while (prevStation.isPresent()) {
            currentUpStation = prevStation.get();
            prevStation = findBeforeStation(currentUpStation);
        }

        return currentUpStation;
    }

    private Optional<Station> findBeforeStation(Station upStation) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(upStation))
                .findFirst()
                .map(Section::getUpStation);
    }

    private Optional<Station> findAfterStation(Station downStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(downStation))
                .findFirst()
                .map(Section::getDownStation);
    }


    public void remove(Station station) {
        validSectionSize();

        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }
    }

    public Lines findLinesFrom(List<Station> stations) {
        return Lines.of(sections.stream()
                .filter(it -> stations.contains(it.getUpStation()) && stations.contains(it.getDownStation()))
                .map(Section::getLine)
                .distinct()
                .collect(Collectors.toList()));
    }

    public LineFare findMaxLineFare() {
        // Lines 조회 분리하지 않고, 구간에서 바로 Line 찾고 LineFare 찾기
        return this.sections
                .stream()
                .map(s -> s.getFare())
                .max(Comparator.comparing(it -> it))
                .orElse(LineFare.zero());

    }

    private void validSectionSize() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
