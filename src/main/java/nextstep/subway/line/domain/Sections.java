package nextstep.subway.line.domain;

import static nextstep.subway.utils.Utils.distinctByKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections () {
    }

    public void add(Section section) {
        if (!contains(section)) {
            sections.add(section);
        }
    }

    public List<Station> getSortedStations() {
        return getSortedSections().stream()
            .flatMap(it -> it.getUpDownStations())
            .filter(distinctByKey(Station::getName))
            .collect(Collectors.toList());
    }

    public void remove(Section section) {
        section.removeLine();
        sections.remove(section);
    }

    public boolean contains(Section section) {
        return sections.stream()
            .anyMatch(it -> it.equalsStations(section));
    }

    public boolean isEmptyStation() {
        return getStationStream().collect(Collectors.toList()).isEmpty();
    }

    public boolean isMinSize() {
        return sections.size() <= MIN_SIZE;
    }

    public Optional<Section> findByUpStation(Station station) {
        return sections.stream()
            .filter(it -> it.isUpStation(station))
            .findFirst();
    }

    public Optional<Section> findByDownStation(Station station) {
        return sections.stream()
            .filter(it -> it.isDownStation(station))
            .findFirst();
    }

    public void updateSection(Section section) {
        updateByUpStation(section);
        updateByDownStation(section);
    }

    private void updateByUpStation(Section section) {
        sections.stream()
            .filter(it -> it.isUpStationOfSection(section))
            .findFirst()
            .ifPresent(it -> it.updateUpStationBySection(section));
    }

    private void updateByDownStation(Section section) {
        sections.stream()
            .filter(it -> it.isDownStationOfSection(section))
            .findFirst()
            .ifPresent(it -> it.updateDownStationBySection(section));
    }

    private List<Section> getSortedSections() {
        List<Section> list = new ArrayList<>();
        Section section = findFirstSection();
        list.add(section);

        Optional<Section>  optional = Optional.of(section);
        while (optional.isPresent()) {
            Section finalSection = section;
            optional = sections.stream()
                .filter(finalSection::isTheUpLine)
                .findFirst();
            section = optional.orElse(finalSection);
            optional.ifPresent(list::add);
        }

        return list;
    }

    private Section findFirstSection() {
        Section section = sections.get(0);
        Optional<Section>  optional = Optional.of(section);
        while (optional.isPresent()) {
            Section finalSection = section;
            optional = sections.stream()
                .filter(finalSection::isTheDownLine)
                .findFirst();
            section = optional.orElse(finalSection);
        }
        return section;
    }

    private Stream<Station> getStationStream() {
        return sections.stream()
            .flatMap(Section::getUpDownStations);
    }

    public boolean isAlreadySection(Section section) {
        return sections.stream()
            .anyMatch(it -> it.equalsStations(section));
    }

    public boolean isIncludeStationOfSection(Section section) {
        return getStationStream()
            .anyMatch(section::isIncludeStation);
    }
}
