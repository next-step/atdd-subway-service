package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() { }

    protected void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validateAdd(section);

        resizeNearSections(section);

        sections.add(section);
    }

    public List<Station> getShortestRoute(Station source, Station target) {
        validateShortestRoute(source, target);

        GraphPath shortestGraph = getShortestGraph(source, target);

        return shortestGraph.getVertexList();
    }

    protected Distance calcDistanceBetween(Station source, Station target) {
        validateShortestRoute(source, target);

        GraphPath path = getShortestGraph(source, target);

        return new Distance(path.getWeight());
    }

    private void validateShortestRoute(Station source, Station target) {
        if (!containsStationsExactly(source, target)) {
            throw new IllegalArgumentException("포함되지 않은 역이 있습니다.");
        }
    }

    protected boolean containsStationsExactly(Station ...stations) {
        return Arrays.stream(stations)
                .allMatch(item -> containsStation(item));
    }

    protected boolean containsStation(Station station) {
        return anyMatch(item -> item.containsStation(station));
    }

    protected Optional<Section> removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findByUpStationEquals(station);
        Optional<Section> downLineStation = findByDownStationEquals(station);

        removeSection(upLineStation, downLineStation);

        return createNewSection(upLineStation, downLineStation);
    }

    protected SortedStations toSortedStations() {
        return new SortedStations(sections);
    }

    private Optional<Section> createNewSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance().plus(downLineStation.get().getDistance());

            return Optional.of(new Section(newUpStation, newDownStation, newDistance));
        }
        return Optional.empty();
    }

    private void validateAdd(Section section) {
        if (containsByUpStation(section) && containsByDownStation(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!containsByUpStation(section) && !containsByDownStation(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void resizeNearSections(Section section) {
        if (containsByUpStation(section)) {
            updateUpStationBySameUpStation(section);
        } else if (containsByDownStation(section)) {
            updateDownStationBySameDownStation(section);
        }
    }

    private Optional<Section> findByUpStationEquals(Station station) {
        return findFirst(item -> item.isUpStationEquals(station));
    }

    private Optional<Section> findByDownStationEquals(Station station) {
        return findFirst(item -> item.isDownStationEquals(station));
    }

    private boolean containsByUpStation(Section section) {
        return anyMatch(item -> item.containsByUpStation(section));
    }

    private boolean containsByDownStation(Section section) {
        return anyMatch(item -> item.containsByDownStation(section));
    }

    private void updateUpStationBySameUpStation(Section section) {
        findFirst(item -> item.isSameUpStation(section))
                .ifPresent(item -> item.updateUpStation(section));
    }

    private void updateDownStationBySameDownStation(Section section) {
        findFirst(item -> item.isSameDownStation(section))
                .ifPresent(item -> item.updateDownStation(section));
    }

    private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> remove(it));
        downLineStation.ifPresent(it -> remove(it));
    }

    private void remove(Section section) {
        sections.remove(section);
    }

    private Optional<Section> findFirst(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findFirst();
    }

    private boolean anyMatch(Predicate<Section> predicate) {
        return sections.stream()
                .anyMatch(predicate);
    }

    private GraphPath getShortestGraph(Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        sections.stream()
                .forEach(item -> item.prepareShortestDistance(graph));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return path;
    }
}
