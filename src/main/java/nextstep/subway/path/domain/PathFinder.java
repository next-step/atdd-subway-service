package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {
  private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;
  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

  private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph, DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
    this.shortestPath = shortestPath;
    this.graph = graph;
  }

  public static PathFinder of(List<Line> lines) {
    WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    for (Line line : lines) {
      addVertexFromLine(graph, line);
      addEdgeFromLine(graph, line);
    }

    return new PathFinder(graph, new DijkstraShortestPath<>(graph));
  }

  private static void addEdgeFromLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
    for (Section section : line.getSections()) {
      section.addGraphEdge(graph);
    }
  }

  private static void addVertexFromLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
    for (Station station : line.getStations()) {
      graph.addVertex(station);
    }
  }

  public List<Station> findShortestPath(Station sourceStation, Station targetStation) {
    Optional<GraphPath<Station, DefaultWeightedEdge>> graphPath = findGraphPath(sourceStation, targetStation);
    return graphPath
            .map(GraphPath::getVertexList)
            .orElseThrow(this::connectionIsNotValid);
  }

  public int findShortestDistance(Station sourceStation, Station targetStation) {
    Optional<GraphPath<Station, DefaultWeightedEdge>> graphPath = findGraphPath(sourceStation, targetStation);
    return graphPath
            .map(GraphPath::getWeight)
            .orElseThrow(this::connectionIsNotValid)
            .intValue();
  }

  private Optional<GraphPath<Station, DefaultWeightedEdge>> findGraphPath(Station sourceStation, Station targetStation) {
    checkDuplicateStation(sourceStation, targetStation);
    checkExistStations(sourceStation, targetStation);
    Optional<GraphPath<Station, DefaultWeightedEdge>> graphPath = Optional.ofNullable(shortestPath.getPath(sourceStation, targetStation));
    return graphPath;
  }

  private void checkExistStations(Station... stations) {
    for (Station station : stations) {
      checkStationInGraph(station);
    }
  }

  private void checkStationInGraph(Station station) {
    if (!graph.containsVertex(station)) {
      throw new IllegalArgumentException(station.getName() + ": 존재하지 않은 지하철 역입니다.");
    }
  }

  private void checkDuplicateStation(Station sourceStation, Station targetStation) {
    if (sourceStation.equals(targetStation)) {
      throw new IllegalArgumentException("출발역과 도착역이 같습니다. 입력 지하철역: " + sourceStation.getName());
    }
  }

  private IllegalArgumentException connectionIsNotValid() {
    return new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
  }
}
