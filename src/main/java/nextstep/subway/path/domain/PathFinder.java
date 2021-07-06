package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionNotConnectedException;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.wrapper.AdditionalFare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Comparator;
import java.util.List;

public class PathFinder implements ShortestPathFinder {
    private WeightedMultigraph<Station, SectionEdge> pathGraph;

    private PathFinder(final WeightedMultigraph<Station, SectionEdge> pathGraph) {
        this.pathGraph = pathGraph;
    }

    public static PathFinder init(final Lines lines) {
        WeightedMultigraph<Station, SectionEdge> pathGraph = new WeightedMultigraph<>(SectionEdge.class);
        List<Station> allStations = lines.getAllStations();
        List<Section> allSections = lines.getAllSections();
        addVertexToGraph(pathGraph, allStations);
        addEdgesToGraph(pathGraph, allSections);
        return new PathFinder(pathGraph);
    }

    private static void addVertexToGraph(final WeightedMultigraph<Station, SectionEdge> pathGraph, final List<Station> wholeStations) {
        wholeStations.forEach(pathGraph::addVertex);
    }

    private static void addEdgesToGraph(final WeightedMultigraph<Station, SectionEdge> pathGraph, final List<Section> wholeSections) {
        wholeSections.forEach(section -> {
            SectionEdge sectionEdge = new SectionEdge(section);
            if (pathGraph.addEdge(sectionEdge.getSourceVertex(), sectionEdge.getTargetVertex(), sectionEdge)) {
                pathGraph.setEdgeWeight(sectionEdge, sectionEdge.getEdgeWeight());
            }
        });
    }

    @Override
    public Path findShortestPath(final Station sourceStation, final Station targetStation) {
        GraphPath<Station, SectionEdge> shortestPath = findPathGraph(sourceStation, targetStation);
        throwIfNotConnectedStations(shortestPath);
        return new Path(shortestPath.getVertexList(), new Double(shortestPath.getWeight()).intValue(),
                getHighestLineAdditionalFareInPath(shortestPath.getEdgeList()));
    }

    private GraphPath<Station, SectionEdge> findPathGraph(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, SectionEdge> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
        try {
            return shortestPathFinder.getPath(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new StationNotExistException();
        }
    }

    private int getHighestLineAdditionalFareInPath(List<SectionEdge> edges) {
        return edges.stream()
                .map(SectionEdge::getSectionLineAdditionalFare)
                .max(Comparator.comparing(AdditionalFare::getFare))
                .map(AdditionalFare::getFare)
                .orElse(0);
    }

    private void throwIfNotConnectedStations(GraphPath<Station, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new SectionNotConnectedException();
        }
    }
}
