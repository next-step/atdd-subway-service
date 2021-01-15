package nextstep.subway.path.application;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@Service
public class PathFinder {

	public PathResponse getDijkstraShortestPath(List<Line> lines,
		Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		lines.stream()
			.map(line -> line.getStations())
			.flatMap(Collection::stream)
			.forEach(station -> graph.addVertex(station));

		lines.stream()
			.map(line -> line.getSections())
			.flatMap(Collection::stream)
			.forEach(section -> {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance());
			});

		/*

		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);*/

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath graphPathResult = dijkstraShortestPath.getPath(sourceStation, targetStation);
		List<Station> shortestPath = graphPathResult.getVertexList();
		int distance = (int) graphPathResult.getWeight();

		List<PathStationResponse> pathStationResponses = shortestPath.stream()
			.map(station -> PathStationResponse.of(station))
			.collect(Collectors.toList());

		//assertThat(shortestPath.size()).isEqualTo(3);
		return PathResponse.of(pathStationResponses, distance);
	}
}
