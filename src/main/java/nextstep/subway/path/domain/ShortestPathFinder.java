package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShortestPathFinder {
	public PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
		lines.stream().flatMap(line -> line.getStations().stream())
				.distinct().forEach(graph::addVertex);
		lines.stream().flatMap(line -> line.getSections().stream())
				.distinct().forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()).setExtraCharge(section.getLine().getExtraCharge()), section.getDistance()));

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

		List<SectionEdge> edges = shortestPath.getEdgeList();
		BigDecimal extraCharge = edges.stream().map(SectionEdge::getExtraCharge).max(BigDecimal::compareTo).get();
		return PathResponse.of(shortestPath.getVertexList(), Double.valueOf(shortestPath.getWeight()).intValue(), extraCharge);
	}
}
