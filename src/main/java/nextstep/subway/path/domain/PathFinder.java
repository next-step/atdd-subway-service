package nextstep.subway.path.domain;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.exception.StationsNotConnectedException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PathFinder implements ShortestPathFinder {

  private Map<Long, Station> wholeStations;
  private WeightedMultigraph<Long, SectionEdge> pathGraph;

  private PathFinder(Map<Long, Station> wholeStations, WeightedMultigraph<Long, SectionEdge> pathGraph) {
    this.wholeStations = wholeStations;
    this.pathGraph = pathGraph;
  }

  public static PathFinder init(Lines lines) {
    WeightedMultigraph<Long, SectionEdge> pathGraph = new WeightedMultigraph<>(SectionEdge.class);
    List<Station> allStations = lines.getAllStations();
    List<Section> allSections = lines.getAllSections();
    addVertexToGraph(pathGraph, allStations);
    addEdgesToGraph(pathGraph, allSections);
    return new PathFinder(collectThroughId(allStations), pathGraph);
  }

  private static void addVertexToGraph(WeightedMultigraph<Long, SectionEdge> pathGraph, List<Station> wholeStations) {
    wholeStations.stream()
        .map(Station::getId)
        .forEach(pathGraph::addVertex);
  }

  private static void addEdgesToGraph(WeightedMultigraph<Long, SectionEdge> pathGraph, List<Section> wholeSections) {
    wholeSections.forEach(section -> {
      SectionEdge sectionEdge = new SectionEdge(section);
      if (pathGraph.addEdge(sectionEdge.getSourceVertex(), sectionEdge.getTargetVertex(), sectionEdge)) {
        pathGraph.setEdgeWeight(sectionEdge, sectionEdge.getEdgeWeight());
      }
    });
  }

  private static Map<Long, Station> collectThroughId(List<Station> wholeStations) {
    return wholeStations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
  }

  @Override
  public Path findShortestPath(Long sourceStationId, Long targetStationId) {
    GraphPath<Long, SectionEdge> shortestPath = findPathGraph(sourceStationId, targetStationId);
    throwIfNotConnectedStations(shortestPath);
    return new Path(getShortestPathStations(shortestPath.getVertexList()), shortestPath.getWeight());
  }

  private GraphPath<Long, SectionEdge> findPathGraph(Long sourceStationId, Long targetStationId) {
    DijkstraShortestPath<Long, SectionEdge> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
    try {
      return shortestPathFinder.getPath(sourceStationId, targetStationId);
    } catch (IllegalArgumentException e) {
      throw new StationNotExistException(e);
    }
  }

  private void throwIfNotConnectedStations(GraphPath<Long, SectionEdge> graphPath) {
    if (graphPath == null) {
      throw new StationsNotConnectedException();
    }
  }

  private List<Station> getShortestPathStations(List<Long> shortestPathStationIds) {
    return shortestPathStationIds.stream()
            .map(stationId -> wholeStations.get(stationId))
            .collect(Collectors.toList());
  }
}
