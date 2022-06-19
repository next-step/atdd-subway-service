package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import nextstep.subway.exception.NotExistException;
import nextstep.subway.exception.NotLinkedPathException;
import nextstep.subway.exception.SamePathException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = null;
    private Set<Section> sections;

    public void init(List<Line> lines) {
        this.sections = new HashSet<>();
        lines.forEach(this::addVertexAndEdge);
    }

    private void addVertexAndEdge(Line line) {
        addSections(line.getSections());
        addVertex(line.getSections());
        addEdgeWeight(line.getSections());
    }

    private void addSections(Sections sections) {
        this.sections.addAll(sections.getSections());
    }

    private void addVertex(Sections sections) {
        sections.getStations()
                .forEach(graph::addVertex);
    }

    private void addEdgeWeight(Sections sections) {
        sections.getSections()
                .forEach(it -> graph.setEdgeWeight(addEdge(it), weight(it.getDistance())));
    }

    private double weight(Distance distance) {
        return distance.getDistance();
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.upStation(), section.downStation());
    }

    public Path getDijkstraPath(Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        GraphPath<Station, DefaultWeightedEdge> graphPath = getOptionalDijkstraPath(source, target)
                .orElseThrow(NotLinkedPathException::new);
        List<Station> stations = graphPath.getVertexList();
        return new Path(stations, (int) graphPath.getWeight(), findPathLines(stations));
    }

    private Set<Line> findPathLines(List<Station> stations) {
        return IntStream.range(0, stations.size() - 1)
                .mapToObj(idx -> findLineBySection(stations.get(idx), stations.get(idx + 1)))
                .collect(Collectors.toSet());
    }

    private Line findLineBySection(Station upStation, Station downStation) {
        return this.sections.stream()
                .filter(it -> it.containUpStation(upStation, downStation))
                .filter(it -> it.containDownStation(upStation, downStation))
                .findFirst()
                .orElseThrow(NotExistException::new)
                .getLine();
    }

    public void validatePath(Station source, Station target) {
        validateSameSourceAndTarget(source, target);
        getDijkstraPath(source, target);
    }

    private Optional<GraphPath<Station, DefaultWeightedEdge>> getOptionalDijkstraPath(Station source, Station target) {
        if (dijkstraShortestPath == null) {
            dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        }
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target));
    }

    private void validateSameSourceAndTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new SamePathException();
        }
    }
}
