package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    private static final int SECTION_MIN_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(final Section section) {
        if (!sections.isEmpty()) {
            validateAddable(section);

            sections.stream()
                .filter(it -> it.isOverlapped(section))
                .findAny()
                .ifPresent(it -> it.divideBy(section));
        }

        sections.add(section);
    }

    private void validateAddable(final Section section) {
        final Set<Station> allStations = extractAllStations();
        final List<Station> sectionStations = section.getStations();

        final Set<Station> matchedStations = sectionStations.stream()
            .filter(allStations::contains)
            .collect(Collectors.toSet());

        if (matchedStations.isEmpty()) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
        if (matchedStations.containsAll(sectionStations)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    public void removeStation(final Station station) {
        validateDeletable(station);

        final Iterator<Section> matchedIterator = sections.stream()
            .filter(it -> it.getUpStation().equals(station) || it.getDownStation().equals(station))
            .iterator();
        final Section toBeDeleted = matchedIterator.next();
        if (matchedIterator.hasNext()) {
            matchedIterator.next().connectWith(toBeDeleted);
        }
        sections.remove(toBeDeleted);
    }

    private void validateDeletable(final Station station) {
        if (sections.size() <= SECTION_MIN_SIZE) {
            throw new IllegalArgumentException("노선의 마지막 구간은 삭제할 수 없습니다.");
        }

        final Set<Station> allStations = extractAllStations();
        if (!allStations.contains(station)) {
            throw new IllegalArgumentException("노선에 등록되어 있지 않은 역은 삭제할 수 없습니다.");
        }
    }

    public List<Station> computeSortedStations() {
        final List<Station> sortedStations = new ArrayList<>();
        final Section upEdgeSection = computeUpEdgeSection();
        sortedStations.add(upEdgeSection.getUpStation());
        sortedStations.add(upEdgeSection.getDownStation());

        final List<Station> nextStations = computeNextSections(upEdgeSection).stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());

        sortedStations.addAll(nextStations);

        return sortedStations;
    }

    private Section computeUpEdgeSection() {
        final Set<Station> downStations = extractDownStations();

        return sections.stream()
            .filter(section -> !downStations.contains(section.getUpStation()))
            .findAny()
            .orElseThrow(() -> new IllegalStateException("지하철 노선에 상행 종점 구간이 존재하지 않습니다."));
    }

    private List<Section> computeNextSections(final Section previousSection) {
        final List<Section> nextSections = new ArrayList<>();

        final Optional<Section> nextSection = sections.stream()
            .filter(section -> section.isNextSection(previousSection))
            .findAny();

        if (nextSection.isPresent()) {
            nextSections.add(nextSection.get());
            nextSections.addAll(computeNextSections(nextSection.get()));
        }

        return nextSections;
    }

    private Set<Station> extractAllStations() {
        return sections.stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .collect(Collectors.toSet());
    }

    private Set<Station> extractDownStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
