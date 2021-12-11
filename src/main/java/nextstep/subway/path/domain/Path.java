package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;

public class Path {

	private List<Station> stations;
	private Distance distance;
	private Price price;

	protected Path() {
	}

	private Path(final List<Station> stations, final Distance distance, final Price price) {
		this.stations = stations;
		this.distance = distance;
		this.price = price;
	}

	public static Path of(List<Line> lines, Station sourceStation, Station targetStation) {
		validSourceTargetStation(sourceStation, targetStation);
		GraphPath<Station, LineWeightedEdge> shortestPath = generateShortestPath(lines, sourceStation,
			targetStation);
		Distance distance = Distance.from((int)shortestPath.getWeight());

		return new Path(shortestPath.getVertexList(), distance, Price.of(distance, shortestPath.getEdgeList()));
	}

	public static Path of(List<Line> lines, Station sourceStation, Station targetStation, LoginMember loginMember) {
		GraphPath<Station, LineWeightedEdge> shortestPath = generateShortestPath(lines, sourceStation,
			targetStation);
		validSourceTargetStation(sourceStation, targetStation);
		Distance distance = Distance.from((int)shortestPath.getWeight());

		if (loginMember.isGuest()) {
			return new Path(shortestPath.getVertexList(), distance, Price.of(distance, shortestPath.getEdgeList()));
		}

		return new Path(shortestPath.getVertexList(), distance,
			Price.of(distance, shortestPath.getEdgeList(), loginMember.getAge()));
	}

	private static boolean isSameSourceTargetStation(Station sourceStation, Station targetStation) {
		return sourceStation.equals(targetStation);
	}

	private static void validSourceTargetStation(Station sourceStation, Station targetStation) {
		if (isSameSourceTargetStation(sourceStation, targetStation)) {
			throw new PathException(ErrorCode.VALID_SAME_SOURCE_TARGET_STATION);
		}
	}

	private static GraphPath<Station, LineWeightedEdge> generateShortestPath(List<Line> lines
		, Station sourceStation
		, Station targetStation) {
		return generateDijkstraShortestPath(generateWeightedMultigraphFromLines(lines), sourceStation, targetStation);
	}

	private static WeightedMultigraph<Station, LineWeightedEdge> generateWeightedMultigraphFromLines(
		List<Line> lines) {
		WeightedMultigraph<Station, LineWeightedEdge> graph = new WeightedMultigraph(
			DefaultWeightedEdge.class);

		lines.forEach(line -> {
			addVertexFromLine(graph, line);
			setEdgeWeightFromLine(graph, line);
		});

		return graph;
	}

	private static void addVertexFromLine(WeightedMultigraph<Station, LineWeightedEdge> graph, Line line) {
		line.getStations()
			.forEach(graph::addVertex);
	}

	private static void setEdgeWeightFromLine(WeightedMultigraph<Station, LineWeightedEdge> graph,
		Line line) {
		line.getSections()
			.forEach(section -> setEdgeWeighted(graph, line, section));
	}

	private static void setEdgeWeighted(WeightedMultigraph<Station, LineWeightedEdge> graph, Line line,
		Section section) {
		LineWeightedEdge lineWeightedEdge = LineWeightedEdge.from(line);
		graph.addEdge(section.getUpStation(), section.getDownStation(), lineWeightedEdge);
		graph.setEdgeWeight(lineWeightedEdge, section.getDistance());
	}

	private static GraphPath<Station, LineWeightedEdge> generateDijkstraShortestPath(
		WeightedMultigraph<Station, LineWeightedEdge> graph, Station sourceStation, Station targetStation) {
		DijkstraShortestPath<Station, LineWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}

	public int getDistance() {
		return distance.getDistance();
	}

	public List<Station> getStations() {
		return stations;
	}

	public Price getPrice() {
		return price;
	}
}
