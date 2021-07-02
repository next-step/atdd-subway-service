package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortestPathFinder {
	public PathResponse findPath(List<Section> sections, List<Station> stations, Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		stations.forEach(graph::addVertex);
		sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

		return PathResponse.of(shortestPath.getVertexList(), Double.valueOf(shortestPath.getWeight()).intValue());
	}
}
