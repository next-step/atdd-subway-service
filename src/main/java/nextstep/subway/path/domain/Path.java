package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;

public class Path {

	private List<Station> stations;
	private Distance distance;

	protected Path() {
	}

	private Path(final List<Station> stations, final Distance distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static Path of(List<Line> lines, Station sourceStation, Station targetStation) {
		validSourceTargetStation(sourceStation, targetStation);
		GraphPath<Station, DefaultWeightedEdge> shortestPath = generateShortestPath(lines, sourceStation,
			targetStation);

		return new Path(shortestPath.getVertexList(), Distance.from((int)shortestPath.getWeight()));
	}

	public List<Station> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance.getDistance();
	}

	private static boolean isSameSourceTargetStation(Station sourceStation, Station targetStation) {
		return sourceStation.equals(targetStation);
	}

	private static void validSourceTargetStation(Station sourceStation, Station targetStation) {
		if (isSameSourceTargetStation(sourceStation, targetStation)) {
			throw new PathException(ErrorCode.VALID_SAME_SOURCE_TARGET_STATION);
		}
	}

	private static GraphPath<Station, DefaultWeightedEdge> generateShortestPath(List<Line> lines
		, Station sourceStation
		, Station targetStation) {
		return generateDijkstraShortestPath(generateWeightedMultigraphFromLines(lines), sourceStation, targetStation);
	}

	private static WeightedMultigraph<Station, DefaultWeightedEdge> generateWeightedMultigraphFromLines(
		List<Line> lines) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
			DefaultWeightedEdge.class);

		lines.forEach(line -> {
			addVertexFromLine(graph, line);
			setEdgeWeightFromLine(graph, line);
		});

		return graph;
	}

	private static void addVertexFromLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
		line.getStations()
			.forEach(graph::addVertex);
	}

	private static void setEdgeWeightFromLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
		Line line) {
		line.getSections()
			.forEach(section -> graph.setEdgeWeight(
				graph.addEdge(section.getUpStation(), section.getDownStation())
				, section.getDistance())
			);
	}

	private static GraphPath<Station, DefaultWeightedEdge> generateDijkstraShortestPath(
		WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station sourceStation, Station targetStation) {
		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}

}
