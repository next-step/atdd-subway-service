package nextstep.subway.path.domain;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.exception.StationsNotConnectedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PathFinder implements ShortestPathFinder {

  private Map<Long, Station> wholeStations;
  private WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph;

  private PathFinder(Map<Long, Station> wholeStations, WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph) {
    this.wholeStations = wholeStations;
    this.pathGraph = pathGraph;
  }

  public static PathFinder init(List<Station> wholeStations, List<Section> wholeSections) {
    WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    addVertexToGraph(pathGraph, wholeStations);
    addEdgesToGraph(pathGraph, wholeSections);
    return new PathFinder(collectThroughId(wholeStations), pathGraph);
  }

  private static void addVertexToGraph(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Station> wholeStations) {
    wholeStations.stream()
        .map(Station::getId)
        .forEach(pathGraph::addVertex);
  }

  private static void addEdgesToGraph(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Section> wholeSections) {
    wholeSections.forEach(section -> {
      Station sourceStation = section.getUpStation();
      Station targetStation = section.getDownStation();
      Distance sectionDistance = section.getDistance();
      pathGraph.setEdgeWeight(pathGraph.addEdge(sourceStation.getId(), targetStation.getId()), sectionDistance.intValue());
    });
  }

  private static Map<Long, Station> collectThroughId(List<Station> wholeStations) {
    return wholeStations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
  }

  @Override
  public Path findShortestPath(Long sourceStationId, Long targetStationId) {
    GraphPath<Long, DefaultWeightedEdge> shortestPath = findPathGraph(sourceStationId, targetStationId);
    throwIfNotConnectedStations(shortestPath);
    return new Path(getShortestPathStations(shortestPath.getVertexList()), shortestPath.getWeight());
  }

  private GraphPath<Long, DefaultWeightedEdge> findPathGraph(Long sourceStationId, Long targetStationId) {
    DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
    try {
      return shortestPathFinder.getPath(sourceStationId, targetStationId);
    } catch (IllegalArgumentException e) {
      throw new StationNotExistException(e);
    }
  }

  private void throwIfNotConnectedStations(GraphPath<Long, DefaultWeightedEdge> graphPath) {
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
