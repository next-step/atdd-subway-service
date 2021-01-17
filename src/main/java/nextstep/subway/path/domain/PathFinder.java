package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
	private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;
	private final List<Line> lines;

	public PathFinder(List<Line> lines, List<Station> stations) {
		this.lines = lines;
		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		this.path = new DijkstraShortestPath<>(graph);
		addAllVertex();
		addAllEdgeWeight();
		validate(stations);
	}

	private void validate(List<Station> stations) {
		if (stations.size() != 2) {
			throw new IllegalArgumentException("역 정보가 정상적으로 조회되지 않았습니다.");
		}
		Station source = stations.get(0);
		Station target = stations.get(1);
		if (stations.get(0).equals(stations.get(1))) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
		isConnectPath(source, target);
	}

	private void isConnectPath(Station source, Station target) {
		if (path.getPath(source, target) == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다.");
		}
	}

	private void addAllVertex() {
		List<Station> stations = lines.stream()
				.flatMap(line -> line.getStations().stream())
				.collect(Collectors.toList());
		stations.forEach(graph::addVertex);
	}


	private void addAllEdgeWeight() {
		lines.forEach(this::addAllEdgeWeight);
	}

	private void addAllEdgeWeight(Line line) {
		line.getSections()
				.forEach(section -> graph.setEdgeWeight(createEdge(section), section.getDistance()));
	}

	private DefaultWeightedEdge createEdge(Section section) {
		return graph.addEdge(section.getUpStation(), section.getDownStation());
	}

	public GraphPath<Station, DefaultWeightedEdge> findShortestPath(List<Station> stations) {
		return path.getPath(stations.get(0), stations.get(1));
	}

}
