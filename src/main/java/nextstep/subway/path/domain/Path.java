package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;

public class Path {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private final List<Station> stations;
	private final double distance;

	private Path(List<Line> lines, Station source, Station target) {
		validate(source, target);

		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(this::addSectionIntoGraph);

		final GraphPath<Station, DefaultWeightedEdge> path = getPath(source, target);
		if (null == path) {
			throw new PathNotFoundException();
		}
		this.stations = path.getVertexList();
		this.distance = path.getWeight();
	}

	private void validate(Station source, Station target) {
		if (Objects.equals(source, target)) {
			throw new IllegalArgumentException("경로의 출발역과 도착역은 서로 달라야 합니다.");
		}
	}

	private void addSectionIntoGraph(Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
		graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance()
		);
	}

	private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
		final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
			new DijkstraShortestPath<>(graph);
		try {
			return dijkstraShortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("노선에 포함되지 않은 역입니다.");
		}
	}

	public static Path of(List<Line> lines, Station source, Station target) {
		return new Path(lines, source, target);
	}

	public List<Station> getStations() {
		return stations;
	}

	public double getDistance() {
		return distance;
	}
}
