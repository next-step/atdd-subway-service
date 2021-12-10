package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private static WeightedMultigraph<Long, DefaultWeightedEdge> graph;

	private PathFinder() {
	}

	public static PathFinder create(List<Station> stations, List<Section> sections) {
		graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		stations.stream().forEach(station -> graph.addVertex(station.getId()));
		sections.stream().forEach(section -> graph.setEdgeWeight(
			graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistanceValue()));
		return new PathFinder();

	}

	public List<Long> findShortestPathVertexes(int source, int target) {
		return new DijkstraShortestPath(graph).getPath((long)source, (long)target).getVertexList();
	}

	public Distance findShortestPathDistance(int source, int target) {
		return Distance.of(new DijkstraShortestPath(graph).getPath((long)source, (long)target).getWeight());
	}
}
