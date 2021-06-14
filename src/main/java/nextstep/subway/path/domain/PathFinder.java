package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PathFinder {

  private Map<Long, Station> wholeStations;
  private WeightedMultigraph<Long, Distance> pathGraph;

  private PathFinder(Map<Long, Station> wholeStations, WeightedMultigraph<Long, Distance> pathGraph) {
    this.wholeStations = wholeStations;
    this.pathGraph = pathGraph;
  }

  public static PathFinder init(List<Station> wholeStations, List<Section> wholeSections) {
    WeightedMultigraph<Long, Distance> pathGraph = new WeightedMultigraph<>(Distance.class);
    addVertexToGraph(pathGraph, wholeStations);
    addEdgesToGraph(pathGraph, wholeSections);
    return new PathFinder(collectThroughId(wholeStations), pathGraph);
  }

  private static void addVertexToGraph(WeightedMultigraph<Long, Distance> pathGraph, List<Station> wholeStations) {
    wholeStations.stream()
        .map(Station::getId)
        .forEach(pathGraph::addVertex);
  }

  private static void addEdgesToGraph(WeightedMultigraph<Long, Distance> pathGraph, List<Section> wholeSections) {
    wholeSections.forEach(section -> {
      Station sourceStation = section.getUpStation();
      Station targetStation = section.getDownStation();
      pathGraph.addEdge(sourceStation.getId(), targetStation.getId(), section.getDistance());
    });
  }

  private static Map<Long, Station> collectThroughId(List<Station> wholeStations) {
    return wholeStations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
  }

  public Path findShortestPath(Long sourceStationId, Long targetStationId) {
    DijkstraShortestPath<Long, Distance> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
    GraphPath<Long, Distance> shortestPath = shortestPathFinder.getPath(sourceStationId, targetStationId);
    return new Path(getShortestPathStations(shortestPath.getVertexList()), calculateShortestDistance(shortestPath.getEdgeList()));
  }

  private List<Station> getShortestPathStations(List<Long> shortestPathStationIds) {
    return shortestPathStationIds.stream()
            .map(stationId -> wholeStations.get(stationId))
            .collect(Collectors.toList());
  }

  private Integer calculateShortestDistance(List<Distance> edgeList) {
    return edgeList.stream()
            .map(Distance::intValue)
            .reduce(0, Integer::sum);
  }
}
