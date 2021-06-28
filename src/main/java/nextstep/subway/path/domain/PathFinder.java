package nextstep.subway.path.domain;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.exception.StationsNotConnectedException;
import nextstep.subway.line.domain.vo.Amount;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Comparator;
import java.util.List;

public class PathFinder implements ShortestPathFinder {

  private WeightedMultigraph<Station, SectionEdge> pathGraph;

  private PathFinder(WeightedMultigraph<Station, SectionEdge> pathGraph) {
    this.pathGraph = pathGraph;
  }

  public static PathFinder init(Lines lines) {
    WeightedMultigraph<Station, SectionEdge> pathGraph = new WeightedMultigraph<>(SectionEdge.class);
    List<Station> allStations = lines.getAllStations();
    List<Section> allSections = lines.getAllSections();
    addVertexToGraph(pathGraph, allStations);
    addEdgesToGraph(pathGraph, allSections);
    return new PathFinder(pathGraph);
  }

  private static void addVertexToGraph(WeightedMultigraph<Station, SectionEdge> pathGraph, List<Station> wholeStations) {
    wholeStations.forEach(pathGraph::addVertex);
  }

  private static void addEdgesToGraph(WeightedMultigraph<Station, SectionEdge> pathGraph, List<Section> wholeSections) {
    wholeSections.forEach(section -> {
      SectionEdge sectionEdge = new SectionEdge(section);
      if (pathGraph.addEdge(sectionEdge.getSourceVertex(), sectionEdge.getTargetVertex(), sectionEdge)) {
        pathGraph.setEdgeWeight(sectionEdge, sectionEdge.getEdgeWeight());
      }
    });
  }

  @Override
  public Path findShortestPath(Station sourceStation, Station targetStation) {
    GraphPath<Station, SectionEdge> shortestPath = findPathGraph(sourceStation, targetStation);
    throwIfNotConnectedStations(shortestPath);
    return new Path(shortestPath.getVertexList(), shortestPath.getWeight(), getHighestLineAdditionalFeeInPath(shortestPath.getEdgeList()));
  }

  private GraphPath<Station, SectionEdge> findPathGraph(Station sourceStation, Station targetStation) {
    DijkstraShortestPath<Station, SectionEdge> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
    try {
      return shortestPathFinder.getPath(sourceStation, targetStation);
    } catch (IllegalArgumentException e) {
      throw new StationNotExistException(e);
    }
  }

  private Amount getHighestLineAdditionalFeeInPath(List<SectionEdge> edges) {
    return edges.stream()
            .map(SectionEdge::getSectionLineAdditionalFee)
            .max(Comparator.comparing(Amount::getAmount))
            .orElse(Amount.ZERO_AMOUNT);
  }

  private void throwIfNotConnectedStations(GraphPath<Station, SectionEdge> graphPath) {
    if (graphPath == null) {
      throw new StationsNotConnectedException();
    }
  }
}
