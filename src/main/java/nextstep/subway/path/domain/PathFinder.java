package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.common.exception.NotConnectedLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class PathFinder {

	private List<Line> lines;
	private Station sourceStation;
	private Station targetStation;

	public PathFinder(List<Line> lines, Station sourceStation, Station targetStation) {
		this.lines = lines;
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public PathResponse getDijkstraShortestPath() {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		generateVertex(lines, graph);
		generateEdgeWeight(lines, graph);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		GraphPath graphPathResult;
		try {
			graphPathResult = dijkstraShortestPath.getPath(sourceStation, targetStation);
		} catch (IllegalArgumentException e) {
			throw new NotConnectedLineException();
		}

		List<Station> shortestPath = graphPathResult.getVertexList();
		int distance = (int)graphPathResult.getWeight();
		int fare = calculateFare(distance);
		List<PathStationResponse> pathStationResponses = shortestPathResultToPathStationResponses(shortestPath);

		return PathResponse.of(pathStationResponses, distance, fare);
	}

	private int calculateFare(int distance) {
		FarePolicy farePolicy = new FarePolicy(distance);
		return farePolicy.calculate();
	}

	private List<PathStationResponse> shortestPathResultToPathStationResponses(List<Station> shortestPath) {
		List<PathStationResponse> pathStationResponses = shortestPath.stream()
			.map(station -> PathStationResponse.of(station))
			.collect(Collectors.toList());
		return pathStationResponses;
	}

	private void generateEdgeWeight(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		lines.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.forEach(section -> {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance());
			});
	}

	private void generateVertex(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		lines.stream()
			.map(Line::getStations)
			.flatMap(Collection::stream)
			.forEach(station -> graph.addVertex(station));
	}
}
