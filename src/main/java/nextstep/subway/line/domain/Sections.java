package nextstep.subway.line.domain;

import nextstep.subway.exception.AlreadyAddSectionException;
import nextstep.subway.exception.NotFoundSectionException;
import nextstep.subway.exception.NotIncludeStationException;
import nextstep.subway.exception.NotRemovableSectionsSize;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTIONS_SIZE = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validateAddSections(section);
            reArrangeAddSections(section);
        }
        sections.add(section);
    }

    private void reArrangeAddSections(Section addSection) {
        sections.stream()
                .filter(section -> section.isIncludeSection(addSection))
                .findFirst()
                .ifPresent(section -> section.updateStationByAddSection(addSection));
    }

    private void validateAddSections(Section section) {
        Stations stations = this.getStations();
        boolean isUpStationExisted = stations.isIncluded(section.getUpStation());
        boolean isDownStationExisted = stations.isIncluded(section.getDownStation());
        alreadyAddSection(isUpStationExisted, isDownStationExisted);
        notIncludeOneStation(isUpStationExisted, isDownStationExisted);
    }

    private void notIncludeOneStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new NotIncludeStationException();
        }
    }

    private void alreadyAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new AlreadyAddSectionException();
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }

        Station upTerminal = findUpTerminal();
        List<Station> sectionStationList = getSectionStationList(upTerminal);

        return new Stations(sectionStationList);
    }

    private List<Station> getSectionStationList(Station station) {
        List<Station> stations = new ArrayList<>();
        while (!Objects.isNull(station)) {
            stations.add(station);
            Section nextSection = findNextSection(station);
            station = nextSection.getDownStation();
        }
        return stations;
    }

    private Section findNextSection(final Station findStation) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(findStation))
                .findFirst()
                .orElse(new Section());
    }

    private Station findUpTerminal() {
        Stations downStations = new Stations(findDownStations());
        return sections.stream().filter(
                section -> !downStations.isIn(section.getUpStation()))
                .findFirst()
                .orElseThrow(NotFoundSectionException::new)
                .getUpStation();
    }

    public void removeByStation(Station station) {
        validateRemovableSize();
        Optional<Section> upLineSection = findByUpStation(station);
        Optional<Section> downLineSection = findByDownStation(station);
        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addNewSection(upLineSection.get(), downLineSection.get());
        }
        upLineSection.ifPresent(sections::remove);
        downLineSection.ifPresent(sections::remove);
    }

    private void addNewSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        int newDistance = upLineSection.getDistance() + downLineSection.getDistance();
        sections.add(new Section(upLineSection.getLine(), newUpStation, newDownStation, newDistance));
    }

    private Optional<Section> findByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(station))
                .findFirst();
    }

    private Optional<Section> findByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsDownStation(station))
                .findFirst();
    }

    private void validateRemovableSize() {
        if (sections.size() < MIN_SECTIONS_SIZE) {
            throw new NotRemovableSectionsSize();
        }
    }

    public PathResponse generatePaths(final Station source, final Station target) {
        PathGraph graph = createGraph();

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph.getGraph());
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        return new PathResponse(graphPath);
    }

    private PathGraph createGraph() {
        PathGraph graph = new PathGraph();
        graph.addGraphVertex(getAllStationsBySections());
        sections.forEach(section ->
                graph.setSectionDistance(
                        graph.addSection(section),
                        section.getDistance()
                )
        );
        return graph;
    }

    private Stations getAllStationsBySections() {
        if (sections.isEmpty()) {
            return new Stations(Collections.emptyList());
        }

        List<Station> distinctStation = findDownStations();
        distinctStation.addAll(findUpStations());
        return new Stations(distinctStation.stream()
                .distinct()
                .collect(Collectors.toList()));
    }

    private List<Station> findDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private List<Station> findUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sections)) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
