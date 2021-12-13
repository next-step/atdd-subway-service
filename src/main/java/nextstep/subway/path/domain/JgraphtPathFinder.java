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

public class JgraphtPathFinder implements PathFinder {
  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

  private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

  public JgraphtPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
    this.graph = graph;
  }

  public void addGraphPropertiesFromLines(List<Line> lines) {
    for (Line line : lines) {
      addVertexFromLine(line);
      addEdgeFromLine(line);
    }

    shortestPath = new DijkstraShortestPath<>(graph);
  }

  private void addEdgeFromLine(Line line) {
    for (Section section : line.getSections()) {
      section.addGraphEdge(graph);
    }
  }

  private void addVertexFromLine(Line line) {
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
    return Optional.ofNullable(shortestPath.getPath(sourceStation, targetStation));
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
