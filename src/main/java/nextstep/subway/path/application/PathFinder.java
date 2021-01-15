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

		generateVertex(lines, graph);
		generateEdgeWeight(lines, graph);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		GraphPath graphPathResult;
		try{
			graphPathResult = dijkstraShortestPath.getPath(sourceStation, targetStation);
		}catch (IllegalArgumentException e){
			throw new RuntimeException("출발역과 도착역이 연결이 되어 있지 않습니다.");
		}

		List<Station> shortestPath = graphPathResult.getVertexList();
		int distance = (int) graphPathResult.getWeight();
		List<PathStationResponse> pathStationResponses = shortestPathResultToPathStationResponses(shortestPath);

		return PathResponse.of(pathStationResponses, distance);
	}

	private List<PathStationResponse> shortestPathResultToPathStationResponses(List<Station> shortestPath) {
		List<PathStationResponse> pathStationResponses = shortestPath.stream()
			.map(station -> PathStationResponse.of(station))
			.collect(Collectors.toList());
		return pathStationResponses;
	}

	private void generateEdgeWeight(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		lines.stream()
			.map(line -> line.getSections())
			.flatMap(Collection::stream)
			.forEach(section -> {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance());
			});
	}

	private void generateVertex(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		lines.stream()
			.map(line -> line.getStations())
			.flatMap(Collection::stream)
			.forEach(station -> graph.addVertex(station));
	}
}
