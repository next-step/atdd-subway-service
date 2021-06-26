package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class StationGraph {

	WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;

	public StationGraph(List<Line> lines) {
		stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

		// todo : 심각하다
		for (Line line : lines) {
			for (Station station : line.getStations()) {
				stationGraph.addVertex(station);
			}
			for (Section section : line.getSections().getSections()) {
				stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance().value());
			}
		}
	}

	public GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station sourceStation, Station targetStation) {
		List<GraphPath<Station, DefaultWeightedEdge>> paths = getPaths(sourceStation, targetStation);
		return getMinPath(paths);
	}

	private GraphPath<Station, DefaultWeightedEdge> getMinPath(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
		double minWeight = getMinWeight(paths);
		return paths.stream().filter(path -> path.getWeight() == minWeight).findFirst().get();
	}

	private Double getMinWeight(List<GraphPath<Station, DefaultWeightedEdge>> paths) {
		return paths.stream()
			.map(GraphPath::getWeight)
			.collect(Collectors.toList())
			.stream()
			.min(Double::compareTo)
			.orElseThrow(RuntimeException::new);
	}

	private List<GraphPath<Station, DefaultWeightedEdge>> getPaths(Station sourceStation, Station targetStation) {
		return new KShortestPaths(stationGraph, 100).getPaths(sourceStation, targetStation);
	}

}
