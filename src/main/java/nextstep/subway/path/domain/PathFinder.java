package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

  private WeightedMultigraph<Long, Distance> pathGraph;

  private PathFinder(WeightedMultigraph<Long, Distance> pathGraph) {
    this.pathGraph = pathGraph;
  }

  public static PathFinder init(List<Station> wholeStations, List<Section> wholeSections) {
    WeightedMultigraph<Long, Distance> pathGraph = new WeightedMultigraph<>(Distance.class);
    addVertexToGraph(pathGraph, wholeStations);
    addEdgesToGraph(pathGraph, wholeSections);
    return new PathFinder(pathGraph);
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

  public Path findShortestPath(Long sourceStationId, Long targetStationId) {
    DijkstraShortestPath<Long, Distance> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
    GraphPath<Long, Distance> shortestPath = shortestPathFinder.getPath(sourceStationId, targetStationId);
    return new Path(shortestPath.getVertexList(), calculateShortestDistance(shortestPath.getEdgeList()));
  }

  private Integer calculateShortestDistance(List<Distance> edgeList) {
    return edgeList.stream()
            .map(Distance::intValue)
            .reduce(0, Integer::sum);
  }
}
